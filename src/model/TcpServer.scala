package model

import java.net.InetSocketAddress
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import play.api.libs.json.{JsValue, Json}


class TcpServer(gameActor: ActorRef) extends Actor {


  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 4242))

  var webServers: Set[ActorRef] = Set()
  var buffer: String = ""
  var delimiter: String = "~"

  override def receive: Receive = {
    case b: Bound =>
      println("Listening on port: " + b.localAddress.getPort)

    case c: Connected =>
      println("Client connected: " + c.remoteAddress)
      this.webServers += sender()
      sender() ! Register(self)

    case PeerClosed =>
      println("Client disconnected: " + sender())
      this.webServers -= sender()

    case r: Received =>
      buffer += r.data.utf8String
      println("Received")
      while (buffer.contains(delimiter)) {
        val curr = buffer.substring(0, buffer.indexOf(delimiter))
        buffer = buffer.substring(buffer.indexOf(delimiter) + 1)
        println(curr)
        handleMessageFromServer(curr)
      }

    case SendGameState =>
      gameActor ! SendGameState

    case gs: GameState =>
      this.webServers.foreach( (client: ActorRef) => client ! Write(ByteString(gs.state + delimiter)) )
  }


  def handleMessageFromServer(json_message: String): Unit = {
    val message: JsValue = Json.parse(json_message)
    val username: String = (message \ "username").as[String]
    val action: String = (message \ "action").as[String]

    action match {
      case "connected" =>
        println("Adding player:   " + username)
        gameActor ! AddPlayer(username)

      case "disconnected" =>
        gameActor ! RemovePlayer(username)

      case "inputs" =>
        val inputs: Map[String, Boolean] = (message \ "inputs").as[Map[String, Boolean]]
        println("TcpServer received inputs")
        gameActor ! Inputs(username, inputs)
    }
  }


}


object TcpServer {


  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()

    import actorSystem.dispatcher
    import scala.concurrent.duration._

    val gameActor = actorSystem.actorOf(Props(classOf[GameActor]))
    val server = actorSystem.actorOf(Props(classOf[TcpServer], gameActor))

    actorSystem.scheduler.schedule(16.milliseconds, 32.milliseconds, gameActor, Update)
    actorSystem.scheduler.schedule(32.milliseconds, 32.milliseconds, server, SendGameState)
  }


}
