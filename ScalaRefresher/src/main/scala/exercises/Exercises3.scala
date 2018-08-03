package exercises

object Exercises3 {
  def main(args: Array[String]): Unit ={
    println(frogjump(10, 85, 30))
    println(frogjump(10, 85, 45))
    println(frogjump(10, 50, 50))
    println(frogjump(1, 5, 1))
  }

  def frogjump(x: Int, y: Int, d: Int): Int ={
    var calc = x
    var count = 0

    while(calc < y) {
      calc = calc + d
      count += 1
    }

    count
  }

}
