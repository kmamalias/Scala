import org.apache.spark.sql.SparkSession
import util.{FileUtil, Verse}

object HiveSQLFileFormats10 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Process Diff File Formats")
      .master("local[*]")
      .config("spark.app.id", "Process Diff File Formats")
      .getOrCreate()
    val sc = spark.sparkContext

    val input_file = "/home/kent/Documents/kjv_copy.txt"

    //Read the text into a table
    val inputRE = """^\s*([^|]+)\s*\|\s*([\d]+)\s*\|\s*([\d]+)\s*\|\s*(.*)~?\s*$""".r
    val versesRDD = sc.textFile(input_file).flatMap{
      case inputRE(book, chapter, verse, text) =>
        Seq(Verse(book, chapter.toInt, verse.toInt, text))
      case line =>
        Console.err.println(s"Unexpected line: $line")
        Nil //Or use Seq.empty[Verse]. I till be eliminated by flattening
    }

    val verses = spark.createDataFrame(versesRDD)
    verses.createTempView("kjv_bible")

    //Save as a Parquet file
    val parquetDir = "output/parquet"
    println(s"Saving 'verses' as a parquet file to $parquetDir")
    FileUtil.rmrf(parquetDir)
    verses.write.parquet(parquetDir)

    //Now read it back and use it as a table
    println(s"Reading in the parquet file from $parquetDir")
    val verses2 = spark.read.parquet(parquetDir)
    verses2.createOrReplaceTempView("verse2")
    verses2.show()

    //Run a SQL query against the table
    println("Using the table from Parquet File, select Jesus verses")
    val jesusVerses = spark.sql("Select * From verse2 Where text Like '%Jesus%'")
    println("Number of Jesus verses: "  + jesusVerses.count())
    jesusVerses.show()

    //Working with JSON
    //Requires each JSON "document" to be on a single line
    //Let's first right some JSON (check the files)
    val jsonDir = "output/json"
    println(s"Saving 'verses' as a JSON file to $jsonDir")
    FileUtil.rmrf(jsonDir)
    verses.write.json(jsonDir)
    val versesJSON = spark.read.json(jsonDir)
    versesJSON.show()

    spark.stop()

  }

}
