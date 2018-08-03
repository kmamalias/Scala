import org.apache.spark.sql.SparkSession
import util.{CommandLineOptions, FileUtil}

object InvertedIndex5b {

  def main(args: Array[String]): Unit = {

    val options = CommandLineOptions(
      this.getClass.getSimpleName,
      CommandLineOptions.inputPath("output/crawl"),
      CommandLineOptions.outputPath("output/inverted-index"),
      CommandLineOptions.master("local"),
      CommandLineOptions.quiet)

    val argz = options(args.toList)
    val master = argz("master")
    val quiet = argz("quiet").toBoolean
    val out = argz("output-path")
    if(master.startsWith("local")){
      if(!quiet) println(s"*** Deleting old output (if any), $out:")
      FileUtil.rmrf(out)
    }
    val name = "Inverted Index (5b)"
    val spark = SparkSession
      .builder
      .master(master)
      .appName(name)
      .config("spark.app.id", name)
      .getOrCreate()
    val sc = spark.sparkContext

    try{
      val lineRE = """^\s*\(([^,]+),(.*)\)\s*$""".r
      val input = sc.textFile(argz("input-path")).map{
        case lineRE(name, text) => (name.trim, text.toLowerCase)
        case badLine =>
          Console.err.println(s"Unexpected line: $badLine")
          ("","")
      }

      if(!quiet) println(s"Writing output to: $out")

      input
        .flatMap{
          case(path, text) => text.trim.split("""[^\p{IsAlphabetic}]+""").map(word => (word, path))}
        .map{
          case(word, path) => ((word, path),1)}
        .reduceByKey((count1, count2) => count1 + count2)
        .map{
          case((word, path), n) => (word, (path, n))}
        .groupByKey()
        .mapValues(iterator => iterator.mkString(", "))
        .saveAsTextFile(out)
    }finally{
      if(!quiet){
        println("""
                  |========================================================================
                  |
                  |    Before closing down the SparkContext, open the Spark Web Console
                  |    http://localhost:4040 and browse the information about the tasks
                  |    run for this example.
                  |
                  |    When finished, hit the <return> key to exit.
                  |
                  |========================================================================
                """.stripMargin)
        Console.in.read()
      }

      spark.stop()

    }



  }

}
