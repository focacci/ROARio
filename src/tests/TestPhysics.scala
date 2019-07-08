package tests

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp.{Bind, Connect}
import model.{Game, GameActor, TcpServer}
import model.gameobjects.player.Player
import model.gameobjects.Platform
import org.scalatest._
import model.physics.{Physics, PhysicsVector}

class TestPhysics extends FunSuite {


  test("Testing:    computeLocation") {
    val location: PhysicsVector = new PhysicsVector(5, 5)
    val velocity: PhysicsVector = new PhysicsVector(0, 1)
    val player: Player = new Player("test", location, velocity)

    val dt: Double = 2

    val pl: PhysicsVector = Physics.computeLocation(player, new Game, dt)

    assert(pl.x == 5)
    assert(pl.y == 7)
  }


  test("Testing:    detectPlatformCollision") {
    val game: Game = new Game

    game.addPlayer("player1")

    game.players("player1").location = new PhysicsVector(5, 5)
    game.players("player1").velocity = new PhysicsVector(0, 1)

    game.addPlatform(new Platform(
      new PhysicsVector(3, 5),
      new PhysicsVector(7, 5)
    ))

    val dt: Double = 2
    val pl: PhysicsVector = Physics.computeLocation(game.players("user1"), game, dt)

    val collision: Boolean = Physics.detectPlatformCollision(game.players("user1"), pl, game)
    assert(collision)
  }

  test("Testing:    gravity") {
    val game: Game = new Game

    game.addPlayer("player1")
    val player = game.players("player1")
    player.location = new PhysicsVector(5, 100)

    val dt: Double = 1.0

    Physics.applyGravity(player, game, dt)
    println(player.velocity.toString)
  }


  test("Testing:  inputs") {
    val inputs: Map[String, Boolean] = Map(
      "w" -> true,
      "a" -> false,
      "s" -> false,
      "d" -> false,
      "space" -> false,
      "up" -> false,
      "down" -> false,
      "left" -> false,
      "right" -> false
    )

    val game: Game = new Game()
    game.addPlayer("focacci")
    println(game.players("focacci").location.toString)

    game.inputs("focacci", inputs)
    game.update(1.0)
    var gs: String = game.state()
    println(gs)
    game.update(1.0)
    gs = game.state()
    println(gs)

  }



}
