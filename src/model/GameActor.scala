package model

import akka.actor.{Actor, ActorRef}


class GameActor extends Actor {

  var players: Map[String, ActorRef] = Map()
  val game: Game = new Game

  override def receive: Receive = {
    case m: AddPlayer => game.addPlayer(m.username)
    case m: RemovePlayer => game.removePlayer(m.username)
    case m: Inputs =>
      println("GameActor received inputs from:  " + m.username)
      handleInputs(m.username, m.inputs)
    case Update =>
      val dt: Double = (System.nanoTime() - game.lastUpdateTime).toDouble
      game.update(dt)
    case SendGameState =>
      sender() ! GameState(game.state())
  }

  def handleInputs(username: String, inputs: Map[String, Boolean]): Unit = {
    game.inputs(username, inputs)
  }

}
