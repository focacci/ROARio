package model

import model.gameobjects.player.Player
import model.gameobjects.{PhysicalObject, Platform}
import model.physics.{Physics, PhysicsVector}

class Game(val host: String) {


  var players: Map[String, Player] = Map()
  addPlayer(host)

  var objects: List[PhysicalObject] = List()
  var platforms: List[Platform] = List()

  val dimensions: Map[String, Int] = Map(
    "width" -> 900,
    "height" -> 600
  )

  var startingLocation: PhysicsVector = new PhysicsVector(dimensions("width").toDouble / 2, dimensions("height").toDouble)

  var lastUpdateTime: Long = System.nanoTime()


  def addPlayer(username: String): Unit = {
    this.players += (username -> new Player(username, startingLocation, new PhysicsVector(0, 0)))
  }

  def removePlayer(username: String): Unit = {
    this.players -= username
  }

  def addPlatform(platform: Platform): Unit = {
    this.platforms = platform :: this.platforms
  }

  def update(): Unit = {
    Physics.update(this)
  }




}
