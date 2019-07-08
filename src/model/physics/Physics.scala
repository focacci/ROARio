package model.physics

import model.Game
import model.gameobjects.PhysicalObject

object Physics {


  val s: Double = 0.000001


  def equal(a: Double, b: Double): Boolean = {
    if (Math.abs(a - b) < this.s) {
      true
    } else
      false
  }

// compute potential locations
  // if not collision, update location
  // else dont change location
  def update(game: Game, dt: Double): Unit = {
    updateLocations(game, dt)
    game.bullets = game.bullets.filter(_.exists)
  }


  def computeLocation(obj: PhysicalObject, game: Game, dt: Double): PhysicsVector = {
    // applyGravity(obj, game, dt)
    println("Velocity is  " + obj.velocity.toString)
    var x: Double = obj.location.x
    var y: Double = obj.location.y

    val px: Double = obj.location.x + (obj.velocity.x * dt)
    val py: Double = obj.location.y + (obj.velocity.y * dt)

    if (px >= 0 && px <= game.width) {
      x = obj.location.x + (obj.velocity.x * dt)
    }

    if (py >= 0 && py <= game.height) {
      y = obj.location.y + (obj.velocity.y * dt)
    }

    val newLocation: PhysicsVector = new PhysicsVector(x, y)
    println("New Location:    " + newLocation.toString)
    newLocation
  }


  def detectPlatformCollision(obj: PhysicalObject, pl: PhysicsVector, game: Game): Boolean = {
    var collision: Boolean = false
    if (obj.velocity.y > 0) {
      for (platform <- game.platforms) {
        if (obj.location.x >= platform.start.x && obj.location.x <= platform.end.x && obj.location.y <= platform.start.y && pl.y >= platform.start.y) {
          collision = true
        }
      }
    }
    collision
  }


  def applyGravity(obj: PhysicalObject, game: Game, dt: Double): Unit = {
    obj.velocity.y += game.gravity * dt
  }


  def updateLocations(game: Game, dt: Double): Unit = {
    for (bullet <- game.bullets) {
      val pl = computeLocation(bullet, game, dt)
      if (detectPlatformCollision(bullet, pl, game)) {
        bullet.destroy()
      } else {
        bullet.location = pl
      }
    }
    for (username <- game.players.keys) {
      val player = game.players(username)
      val pl = computeLocation(player, game, dt)
      if (detectPlatformCollision(player, pl, game)) {
        println("collision detected")
        player.velocity.y = 0
      } else {
        player.location = pl
        println("New location:  " + pl.toString)
      }
    }
  }





}
