case class Vec(val x: Double, val y: Double) {
  def +(that: Vec): Vec = new Vec(this.x + that.x, this.y + that.y)
}

object MainOperators {
  def main(args: Array[String]): Unit = {

    //Operators are methods in scala
    //10 + 1 is equivalent to 10.+(1)
    //You can also use an operator as legal identifier for example the method above def + of class Vec
    val vecA = new Vec(1.0, 2.0)
    val vecB = new Vec(3.0, 4.0)
    println(vecA+vecB)
  }
}
