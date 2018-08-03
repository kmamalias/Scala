import java.io.File

import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StructField, StructType}

object Scala1 {
  def main(args: Array[String]): Unit = {

    //val shakespeare = new File("/home/hduser/JustEnoughScalaForSpark/data/shakespeare")
    val shakespeare = new File("/home/kent/Documents")
    val success = if (shakespeare.exists() == false){
                    error(s"Data directory does not exists! $shakespeare" )
                    false
                  } else{
                    info(s"$shakespeare exists")
                    true
                  }
    val pathSeparator = File.separator
    val targetDirName = shakespeare.toString
    //val plays = Seq("tamingoftheshrew", "comedyoferrors", "loveslabourslost", "midsummersnightsdream",
    //  "merrywivesofwindsor", "muchadoaboutnothing", "asyoulikeit", "twelfthnight")
    val plays = Seq("file1.txt", "file2.txt")

    //println(s"success = $success")

    if(success){
      println(s"Checking that the plays are in $shakespeare:")

      val failures = for{
        play <- plays
        playFileName = targetDirName + pathSeparator + play
        playFile = new File(playFileName)
        if(playFile.exists() == false)
      } yield{
        s"$playFileName:\tNot Found!"
      }

      println("Finished!")
      if(failures.size == 0){
        info("All plays found!")
      } else{
        println("The following expected plays were not found:")
        failures.foreach(play=>error(play))
      }
    }

    /*
    val logFile = "/home/hduser/JustEnoughScalaForSpark/SpeakerNotes.md" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
    */

    //val conf = new SparkConf().setAppName("Simple Application").setMaster("local[*]")
    //val sc = new SparkContext(conf)

    val spark = SparkSession.builder.appName("Simple App").master("local[*]").getOrCreate()
    val sc = spark.sparkContext

    val df = spark.read.json("/usr/local/spark/examples/src/main/resources/people.json")

    df.show()



    /*
    val ii = sc.wholeTextFiles(shakespeare.toString).
      flatMap{
        case(location, contents) =>
          val words = contents.split("""\W+""").filter(word => word.size > 0)
          val fileName = location.split(pathSeparator).last
          words.map(word => ((word.toLowerCase, fileName), 1))
      }.
      reduceByKey((count1, count2) => count1 + count2).
      map{
        case((word, fileName), count) => (word, (fileName, count))
      }.
      groupByKey.
      sortByKey(ascending = true).
      map{
        case(word, iterable) =>
          val vect = iterable.toVector.sortBy{
            case(fileName, count) => (-count, fileName)
          }
          val (locations, counts) = vect.unzip
          val totalCount = counts.reduceLeft((n1, n2) => n1 + n2)
          (word, totalCount, locations, counts)
      }

    import spark.implicits._

    val col = List(
      StructField("word", StringType, true) ,
      StructField("total_count", IntegerType, true)
    )

    //val df = spark.createDataFrame( sc.parallelize(ii), StructType(col))
    val df = spark.createDataFrame(ii)
    val vw = df.createOrReplaceTempView("inverted_index")

    //val iiDF = spark.createDataFrame(ii).toDF("word", "total_count", "locations", "counts")
    //iiDF.createOrReplaceTempView("inverted_index")

    val test = spark.sql(
      """select word, total_count, locations[0] as top_locations, counts[0] as top_counts
        |from inverted_index
      """.stripMargin)
    test.show
*/
    spark.stop()

  }

  def info(message: String): String = {

    println(message)

    message
  }

  def error(message: String): String = {

    val fullMessage = s"""
                         |************************************************************************************************
                         |  ERROR:$message
                         |************************************************************************************************
        """.stripMargin
    println(fullMessage)

    fullMessage
  }
}