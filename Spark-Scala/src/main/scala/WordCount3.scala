import util.{CommandLineOptions, FileUtil, TextUtil}
import org.apache.spark.sql.SparkSession

object WordCount3 {
  def main(args: Array[String]): Unit ={
    val options = CommandLineOptions(
      this.getClass.getSimpleName,
      //CommandLineOptions.inputPath("/home/hduser/spark-scala-tutorial/data/kjvdat.txt"),
      CommandLineOptions.inputPath("/home/kent/Documents/file1.txt"),
      CommandLineOptions.outputPath("output/kjv-wc3"),
      CommandLineOptions.master("local"),
      CommandLineOptions.quiet)

    val argz = options(args.toList)
    val master = argz("master")
    val quiet = argz("quiet").toBoolean
    val in = argz("input-path")
    val out = argz("output-path")

    if(master.startsWith("local")){
      if(!quiet) println(s" *** Deleting old output (if any), $out")
      FileUtil.rmrf(out)
    }

    val name = "Word Count (3)"
    val spark = SparkSession
      .builder
      .master(master)
      .appName(name)
      .config("spark.app.id", name)       //To silence metrics warning
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")   //Kryo Serialization provides better compression therefore better utilization of memory
      .getOrCreate()
    val sc = spark.sparkContext

    try{
      val input = sc.textFile(in).map(line => TextUtil.toText(line))    //Also converts to lowercase

      val wc2a = input
        .flatMap(line => line.split("""[^\p{IsAlphabetic}]+"""))
        .countByValue()         //Returns a Map[T, long], e.g. Map(test -> 1, this -> 2, a -> 3)

      val wc2b = wc2a.map(key_value => s"${key_value._1},${key_value._2}").toSeq  //Returns Iterable[String] = List(test,1, this,2, a,3). The toSeq will convert it to Seq[String] = List(test,1, this,2, a,3)

      val wc2 = sc.makeRDD(wc2b, 1) //Convert the sequence to RDD, 1 is the number of slices/partition

      /*val return1: Array[String] => String = (arr: Array[String]) => arr(0)  //accepts an Array, returns String value of Array[0]

      val wc_sortyByWordLength = wc2
        .map{word_count => (return1(word_count.split(",")), return1(word_count.split(",")).size )}
        .sortBy{case(word, word_size) => (-word_size, word)}
        .map{case(word, word_size) => word}

        if(!quiet)  println(s"Writing output to: $out")
        wc_sortyByWordLength.saveAsTextFile(out)*/


      if(!quiet)  println(s"Writing output to: $out")
      wc2.saveAsTextFile(out)

    }finally {
      spark.stop()
    }

  }
}
