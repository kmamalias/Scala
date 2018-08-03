import org.apache.spark.{SparkConf, SparkContext}

object Main {
  def main(args: Array[String]) {

    //Create a SparkContext to initialize Spark
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("Hello World")
    val sc = new SparkContext(conf)

    println("Hello World")
    println(sc)
  }

}
