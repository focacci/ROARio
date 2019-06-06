package model

import model.gameobjects.player.Player
import model.gameobjects.{PhysicalObject, Platform}
import model.physics.{Physics, PhysicsVector}


class Game(val host: String) {


  var players: Map[String, Player] = Map()

  var objects: List[PhysicalObject] = List()

  var platforms: List[Platform] = List()

  val width: Int = 900
  val height: Int = 600

  var startingLocation: PhysicsVector = new PhysicsVector(width.toDouble / 2, height.toDouble)

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

  def load(): Unit = {
    this.platforms = List(
      new Platform(new PhysicsVector(0, height), new PhysicsVector(width, height)),
      new Platform(new PhysicsVector(0, 400), new PhysicsVector(300, 400)),
      new Platform(new PhysicsVector(0, 200), new PhysicsVector(300, 200)),
      new Platform(new PhysicsVector(300, 300), new PhysicsVector(600, 300)),
      new Platform(new PhysicsVector(300, 500), new PhysicsVector(600, 500)),
      new Platform(new PhysicsVector(600, 400), new PhysicsVector(width, 400)),
      new Platform(new PhysicsVector(600, 200), new PhysicsVector(width, 200))
    )
    
    this.addPlayer(host)
  }



}
