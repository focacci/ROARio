package model.gameobjects

import model.physics.PhysicsVector

class PhysicalObject(var location: PhysicsVector,
                     var velocity: PhysicsVector) {


  var exists: Boolean = true


  def destroy(): Unit = {
    this.exists = false
  }




}
