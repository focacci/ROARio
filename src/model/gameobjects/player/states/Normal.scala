package model.gameobjects.player.states


import model.gameobjects.player.Player


class Normal(player: Player) extends PlayerState(player) {


  override def leftPressed(): Unit = {
    println("left pressed")
    player.velocity.x = -5
  }

  override def leftReleased(): Unit = {
    println("left released")
    player.velocity.x = 0
  }

  override def rightPressed(): Unit = {
    println("right pressed")
    player.velocity.x = 5
  }

  override def rightReleased(): Unit = {
    println("right released")
    player.velocity.x = 0
  }

  override def jumpPressed(): Unit = {
    println("jump pressed")
    player.velocity.y = -5
  }

  override def jumpReleased(): Unit = {
    println("jump released")
    player.velocity.y = 0
  }


}
