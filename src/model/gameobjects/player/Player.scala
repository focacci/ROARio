package model.gameobjects.player

import model.gameobjects.PhysicalObject
import model.physics.PhysicsVector
import model.gameobjects.player.states._


class Player(var username: String,
             in_location: PhysicsVector,
             in_velocity: PhysicsVector)
  extends PhysicalObject(in_location, in_velocity) {


  var speed: Double = 5
  var health: Int = 3
  var state: PlayerState = new Normal(this)

  var rightHeld: Boolean = false
  var leftHeld: Boolean = false
  var jumpHeld: Boolean = false



}
