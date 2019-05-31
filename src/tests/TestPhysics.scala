package tests

import model.Game
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

    val pl: PhysicsVector = Physics.computeLocation(player, dt)

    assert(pl.x == 5)
    assert(pl.y == 7)
  }


  test("Testing:    detectPlatformCollision") {
    val game: Game = new Game("user1")

    val location: PhysicsVector = new PhysicsVector(5, 5)
    val velocity: PhysicsVector = new PhysicsVector(0, 1)

    game.players("user1").location = location
    game.players("user1").velocity = velocity

    game.addPlatform(new Platform(
      new PhysicsVector(3, 5),
      new PhysicsVector(7, 5)
    ))

    val dt: Double = 2

    val pl: PhysicsVector = Physics.computeLocation(game.players("user1"), dt)

    val collision: Boolean = Physics.detectPlatformCollision(game.players("user1"), pl, game)

    assert(collision)
  }




}
