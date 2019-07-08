package model.gameobjects.player.states

import model.gameobjects.player.Player

abstract class PlayerState(player: Player) {


  def leftPressed(): Unit = {}
  def leftReleased(): Unit = {}

  def rightPressed(): Unit = {}
  def rightReleased(): Unit = {}

  def jumpPressed(): Unit = {}
  def jumpReleased(): Unit = {}

  def platformCollision(): Unit = {
    player.velocity.y = 0
  }


}
