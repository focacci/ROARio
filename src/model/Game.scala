package model

import model.gameobjects.player.Player
import model.gameobjects.{PhysicalObject, Platform}
import model.physics.{Physics, PhysicsVector}

class Game {


  var players: Map[String, Player] = Map()
  var level: Map[Int, Level] = Map()

  var objects: List[PhysicalObject] = List()
  var platforms: List[Platform] = List()

  val width: Int = 600
  val height: Int = 900

  var startingLocation: PhysicsVector = new PhysicsVector(this.width.toDouble / 2, this.height.toDouble)

  var lastUpdateTime: Long = System.nanoTime()


  def addPlayer(username: String): Unit = {
    this.players += (username -> new Player(username, startingLocation, new PhysicsVector(0, 0)))
  }

  def removePlayer(username: String): Unit = {
    this.players -= username
  }

  def update(): Unit = {
    Physics.update(this)
  }




}
