import scalaClasses.Point.{Point1, Point2, Point3}

object MainPoint{
  def main(args: Array[String]): Unit ={
    val point1 = new Point1(2,3)
    println(point1.y)
    println(point1)

    val origin = new Point2
    println(origin.x, origin.y)
    val point2_a = new Point2(1)
    println(point2_a.x, point2_a.y)

    val point3 = new Point3
    println(point3.x, point3.y)
    point3.x = 99
    point3.y = 101
    println(point3.x, point3.y)
  }
}
