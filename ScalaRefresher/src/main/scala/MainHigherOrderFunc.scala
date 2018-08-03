case class WeeklyWeatherForecast(temperatures: Seq[Double]){
  private def convertCtoF(temp: Double) = temp * 1.8 + 32

  def forecastInFahrenheit: Seq[Double] = temperatures.map(convertCtoF)
}

object MainHigherOrderFunc {
  def main(args: Array[String]): Unit ={
    val t = Seq(32.2, 12.3, 14.89)
    val tempSeq = new WeeklyWeatherForecast(t)
    tempSeq.forecastInFahrenheit.foreach(e => println(e))
  }
}
