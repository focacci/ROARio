package model

import model.gameobjects.bullet.Bullet
import model.gameobjects.player.Player
import model.gameobjects.{PhysicalObject, Platform}
import model.physics.{Physics, PhysicsVector}
import play.api.libs.json.{JsValue, Json}


class Game {

  var players: Map[String, Player] = Map()

  var bullets: List[Bullet] = List()

  val width: Int = 900
  val height: Int = 600

  var gravity: Int = 10

  var platforms: List[Platform] = List(
    new Platform(new PhysicsVector(0, height), new PhysicsVector(width, height)),
    new Platform(new PhysicsVector(0, 400), new PhysicsVector(300, 400)),
    new Platform(new PhysicsVector(0, 200), new PhysicsVector(300, 200)),
    new Platform(new PhysicsVector(300, 300), new PhysicsVector(600, 300)),
    new Platform(new PhysicsVector(300, 500), new PhysicsVector(600, 500)),
    new Platform(new PhysicsVector(600, 400), new PhysicsVector(width, 400)),
    new Platform(new PhysicsVector(600, 200), new PhysicsVector(width, 200))
  )

  var startingLocation: PhysicsVector = new PhysicsVector(width.toDouble / 2, height.toDouble - 1)

  var lastUpdateTime: Long = System.nanoTime()


  def addPlayer(username: String): Unit = {
    this.players += (username -> new Player(username, startingLocation, new PhysicsVector(0, 0)))
    println(username + " joined")
  }

  def removePlayer(username: String): Unit = {
    this.players -= username
    println(username + " left the game")
  }

  def addPlatform(platform: Platform): Unit = {
    this.platforms = platform :: this.platforms
  }

  def update(dt: Double): Unit = {
    Physics.update(this, dt)
    this.lastUpdateTime = System.nanoTime()
  }

  def state(): String = {
    val gameState: Map[String, JsValue] = Map(
      "dimensions" -> Json.toJson(Map(
        "width" -> width,
        "height" -> height)),
      "platforms" -> Json.toJson(this.platforms.map({ platform => Json.toJson(Map(
        "start" -> Map(
          "x" -> platform.start.x,
          "y" -> platform.start.y),
        "end" -> Map(
          "x" -> platform.end.x,
          "y" -> platform.end.y)))})),
      "players" -> Json.toJson(this.players.map({ case (name, player) => Json.toJson(Map(
        "location" -> Json.toJson(Map(
          "x" -> Json.toJson(player.location.x),
          "y" -> Json.toJson(player.location.y))),
        "velocity" -> Json.toJson(Map(
          "vx" -> Json.toJson(player.velocity.x),
          "vy" -> Json.toJson(player.velocity.y))),
        "health" -> Json.toJson(this.players(name).health),
        "username" -> Json.toJson(name)))})),
      "bullets" -> Json.toJson(this.bullets.map({ bullet => Json.toJson(Map(
        "x" -> bullet.location.x,
        "y" -> bullet.location.y))}))
    )
    Json.stringify(Json.toJson(gameState))
  }

  def inputs(username: String, inputs: Map[String, Boolean]): Unit = {
    val player = this.players(username)

    if (inputs("w") && !inputs("s")) {
      println("W pressed")
      player.jumpHeld = true
      player.state.jumpPressed()
    }

    else if (!inputs("w") && inputs("s")) {
      println("S pressed")
      player.jumpHeld = false
      player.state.jumpReleased()
    }

    else if (!inputs("w") && !inputs("s")) {
      player.jumpHeld = false
      player.state.jumpReleased()
    }

    if (inputs("a") && !inputs("d")) {
      println("A pressed")
      player.leftHeld = true
      player.state.leftPressed()
    }

    else if (!inputs("a") && inputs("d")) {
      println("D pressed")
      player.rightHeld = true
      player.state.rightPressed()
    }

    else if (!inputs("a") && !inputs("d")) {
      player.leftHeld = false
      player.state.leftReleased()
      player.rightHeld = false
      player.state.rightReleased()
    }
  }


}
