import org.apache.spark.sql.SparkSession
import util.{CommandLineOptions, FileUtil, Verse}

object SparkSQL8 {
  def main(args: Array[String]): Unit = {
    val options = CommandLineOptions(
      this.getClass.getSimpleName,
      CommandLineOptions.inputPath("/home/kent/Documents/kjv_copy.txt"),
      CommandLineOptions.outputPath("output/kjv-spark-sql"),
      CommandLineOptions.master("local[2]"),
      CommandLineOptions.quiet)

    val argz = options(args.toList)
    val master = argz("master")
    val quiet = argz("quiet").toBoolean
    val out = argz("output-path")
    val outgv = s"$out-god-verses"
    val outvpb = s"$out-verses-per-book"

    if(master.startsWith("local")){
      if(!quiet){
        println(s" ***Deleting old output (if any), $outgv:")
        println(s" ***Deleting old output (if any), $outvpb:")
      }
      FileUtil.rmrf(outgv)
      FileUtil.rmrf(outvpb)
    }

    val name = "Spark SQL (8)"
    val spark = SparkSession
      .builder
      .master(master)
      .appName(name)
      .config("spark.app.id", name)
      .getOrCreate()
    val sc = spark.sparkContext

    import spark.implicits._

    try{
      //Regex to match the fields separated by '|', and strip the trailing '~' at the end of the line
      val lineRE = """^\s*([^|]+)\s*\|\s*([\d]+)\s*\|\s*([\d]+)\s*\|\s*(.*)~?\s*$""".r

      //Use flatMap to effectively remove bad lines
      val versesRDD = sc.textFile(argz("input-path"))
        .flatMap{
          case lineRE(book, chapter, verse, text) =>
            Seq(Verse(book, chapter.toInt, verse.toInt, text))
          case line =>
            Console.err.println(s"Unexpected line: $line")
            Seq.empty[Verse]  //Will be eliminated by flattening
        }

      //Create a DataFrame
      val verses = spark.createDataFrame(versesRDD)

      //Create a temporary view.
      //The following expression invokes several "implicit" conversions and methods that we imported through spark._
      //The actual method is defined on org.apache.spark.sql.SchemaRDDLike, which also has a methord "saveAsParquetFile"
      //to write a schema-preserving Parquet file
      verses.createTempView("kjv_bible")
      verses.cache()
      if(!quiet){
        verses.show   //print the first 20 lines, default
      }

      import spark.sql    //convenient for running SQL queries

      val godVerses = sql("Select * From kjv_bible Where text Like '%God%'")
      if(!quiet){
        println("The query plan:")
        //Compare with println(godVerses.queryExecution)
        godVerses.explain(true)
        println("Number of verses that mentiond God: " + godVerses.count())
        godVerses.show()
      }

      //Use the DataFrame API
      val godVersesDF = verses.filter(verses("text").contains("God"))
      if(!quiet){
        println("The query plan:")
        godVersesDF.explain(true)
        println("Number of verses that mention God: " + godVersesDF.count())
        godVersesDF.show()
      }

      //Use groupBy and column aliasing
      val counts = sql("Select book, Count(*) As count From kjv_bible Group By book")
      if(!quiet){
        counts.show()
      }

      //Use "coalesce" when you have too many small partitions. The integer passed to "coalesce" is the number
      //out output partitions(1 in this case)
      val counts1 = counts.coalesce(1)
      if(!quiet){
        val nPartitions = counts.rdd.partitions.size
        val nPartitions1 = counts1.rdd.partitions.size
        println(s"counts.count (can take a while, #$nPartitions partitions):")
        println(s"result: ${counts.count}")
        println(s"counts1.count (usually faster!! #$nPartitions1 partitions):")
        println(s"result: ${counts1.count}")
      }

      counts1.rdd.saveAsTextFile(outvpb)


    }finally{
      spark.stop()
    }
  }

}
