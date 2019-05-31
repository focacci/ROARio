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
  def update(game: Game): Unit = {
    val dt: Double = (System.nanoTime() - game.lastUpdateTime).toDouble
    checkPlatformCollisions(game, dt)
    game.lastUpdateTime = System.nanoTime()
  }


  def computeLocation(obj: PhysicalObject, dt: Double): PhysicsVector = {
    new PhysicsVector(
      obj.location.x + (obj.velocity.x * dt),
      obj.location.y + (obj.velocity.y * dt)
    )
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


  def checkPlatformCollisions(game: Game, dt: Double): Unit = {
    for (obj <- game.objects) {
      val pl = computeLocation(obj, dt)
      if (detectPlatformCollision(obj, pl, game)) {
        obj.velocity.y = 0
      }
    }
  }




}
