object MainNestedMethodsFactorial {

  def main(args: Array[String]): Unit = {
    def factorial(x: Int): Int = {
      def fact(x: Int, accumulator: Int): Int = {
        if(x <= 1) accumulator
        else fact(x-1, x*accumulator)
      }
      fact(x, 1)
    }

    println("Factorial of 2: " + factorial(2))
    println("Factorial of 30: " + factorial(30))
  }
}
