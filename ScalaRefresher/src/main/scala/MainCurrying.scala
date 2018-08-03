object MainCurrying {
  def main(args: Array[String]): Unit = {
    val num = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    val sumA = num.foldLeft(0)((m, n) => m + n)
    println(sumA)

    val sumB = num.foldLeft(0)(_+_)
    println(sumB)

    val numFunc = num.foldLeft(List[Int]())_

    val squares = numFunc((xs, x) => xs:+ x*x)
    println(squares)

    val cubes = numFunc((xs, x) => xs:+ x*x*x)
    println(cubes)
  }

}
