object MainHigherOrderFuncSal {

  private def promotion(salaries: List[Double], promotionFunc: Double => Double): List[Double] = {
    salaries.map(promotionFunc)
  }

  def smallPromotion(salaries: List[Double]): List[Double] = {
    promotion(salaries, salary => salary * 1.1)
  }

  def bigPromotion(salaries: List[Double]): List[Double] = {
    promotion(salaries, salary => math.log(salary))
  }

  def hugePromotion(salaries: List[Double]): List[Double] = {
    promotion(salaries, salary => salary * salary)
  }

  def main(args: Array[String]): Unit = {
    val list = List(12.9, 13.7)

    println(smallPromotion(list))
  }

}
