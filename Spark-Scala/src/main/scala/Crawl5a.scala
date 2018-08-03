import java.io.File

import org.apache.spark.sql.SparkSession
import util.CommandLineOptions

object Crawl5a {

  def main(args: Array[String]): Unit ={
    val options = CommandLineOptions(
      this.getClass.getSimpleName,
      CommandLineOptions.inputPath("/home/hduser/spark-scala-tutorial/data/enron-spam-ham/*"),
      CommandLineOptions.outputPath("output/crawl"),
      CommandLineOptions.master("local"),
      CommandLineOptions.quiet)

    val argz = options(args.toList)
    val master = argz("master").toString
    val quiet = argz("quiet").toBoolean
    val out = argz("output-path").toString
    val in = argz("input-path").toString
    val separator = File.pathSeparator
    val name = "Crawl (5a)"
    val spark = SparkSession
      .builder
      .master(master)
      .appName(name)
      .config("spark.app.id", name)
      .getOrCreate()
    val sc = spark.sparkContext

    try{
      val file_contents = sc.wholeTextFiles(argz("input-path"))
      if(!quiet) println(s"Writing ${file_contents.count} lines to: $out")
      file_contents.map{
        case(path, text) =>
          val lastSep = path.lastIndexOf(separator)
          val path2 = if(lastSep < 0) path.trim else path.substring(lastSep+1, path.length).trim
          val text2 = text.trim.replaceAll("""\s*\n\s*""", " ")
          (path2, text2)
      }.saveAsTextFile(out)
    }finally{
      spark.stop()
    }
  }

}
