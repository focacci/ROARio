package model.physics

class PhysicsVector(var x: Double,
                    var y: Double) {


  def distanceFrom(other: PhysicsVector): Double = {
    Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2))
  }


  def unitVector(): PhysicsVector = {
    if (this.x == 0 & this.y == 0) {
      new PhysicsVector(0, 0)
    }
    else {
      val magnitude = Math.sqrt(Math.pow(this.x, 2.0) + Math.pow(this.y, 2.0))
      new PhysicsVector(this.x / magnitude, this.y / magnitude)
    }
  }


  override def toString: String = {
    "(" + this.x.toString + ", " + this.y.toString + ")"
  }




}
