object MainPolymorphicMethods {
  def duplicator[A](value: A, numTimes: Int): List[A] = {
    if(numTimes < 1)
      Nil
    else
      value :: duplicator(value, numTimes-1) //recursive call. prepend value to left of A
  }

  def main(args: Array[String]): Unit = {
    println(duplicator[Int](3, 4)) //here we specify type [Int]
    println(duplicator("la", 8)) //but scala can infer the type of value supplied, this list it is String
  }

}
