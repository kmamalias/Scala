import java.io.File
import org.apache.spark.sql.SparkSession

object Local_InvertedIndex {

  val pathSeparator = File.separator
  val stop_words = Seq("a", "an", "the", "he", "she", "it", "as")

  case class iiRecord(
    word: String,
    total_count: Int=0,
    locations: Array[String] = Array.empty,
    counts: Array[Int] = Array.empty){

    override def toString: String = s"""IIRecord($word, $total_count, $locStr, $cntStr)"""

    def toCSV: String = s"$word,$total_count,$locStr,$cntStr"

    def toJSONString: String =
      s"""{
         |  "word":         "$word",
         |  "total_count":  $total_count,
         |  "locations":    ${toJSONArrayString(locations)},
         |  "counts":       ${toArrayString(counts, ", ")}
         |}""".stripMargin

    private def locStr = toArrayString(locations)

    private def cntStr = toArrayString(counts)

    private def toArrayString(array: Array[_], delim: String =  ","): String = array.mkString("[", delim, "]")

    private def toJSONArrayString(array: Array[String]): String = toArrayString(array.map(quote), ", ")

    private def quote(word: String): String = "\"" + word + "\""
  }

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder.appName("Simple App").master("local[*]").getOrCreate()
    val sc = spark.sparkContext
    val shakespeare = new File("/home/hduser/JustEnoughScalaForSpark/data/shakespeare")
    val plays = Seq("tamingoftheshrew", "comedyoferrors", "loveslabourslost", "midsummersnightsdream", "merrywivesofwindsor", "muchadoaboutnothing", "asyoulikeit", "twelfthnight")
    //val shakespeare = new File("/home/kent/Documents")
    //val plays = Seq("file1.txt", "file2.txt")

    checkFileExists(shakespeare, plays)
    inverted_index(spark, shakespeare)

    spark.stop()
  }

  def checkFileExists(shakespeare: File, plays: Seq[String]): Unit ={

    val success = if (shakespeare.exists() == false){
      error(s"Data directory does not exists! $shakespeare" )
      false
    } else{
      info(s"$shakespeare exists")
      true
    }

    val targetDirName = shakespeare.toString

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
  }

  def inverted_index(spark: SparkSession, shakespeare: File): Unit ={

    val ii = spark.sparkContext.wholeTextFiles(shakespeare.toString).     //create RDD[(directory_filename, file_contents )]
      flatMap{                                                            //current shape: RDD[(directory_filename, file_contents )]
        case(location, "") =>                                             //pattern match, RDD[(directory, NULL)]
          Array.empty[((String, String), Int)]                            //return an empty string if directory has no files
        case(location, contents) =>                                       //pattern match, RDD[(directory_filename, file_contents)]
          val words = contents.split("""\W+""")                           //split each word inside file_contents
          val fileName = location.split(pathSeparator).last               //split each word in directory_filename value and take the last value(filename)
          words.map(word => ((word.toLowerCase, fileName), 1))            //reshape the RDD to [(word, filename), count default set to 1]
      }.
      reduceByKey((count1, count2) => count1 + count2).                   //the key: (word, filename), value: count1 & count2. Sum the value group by the key
      map{                                                                //current shape: RDD[((word, fileName), count)]
        case((word, fileName), count) => (word, (fileName, count))        //reshape RDD to RDD[(word, (fileName, count))]
      }.
      groupByKey.                                                         //group by key: word
      filter{
        case(word, iterable_fileName_count) => (word.size > 0) & (keep(word)) //filter out nulls and stop_words using the function keep. keep is defined below
      }.
      sortByKey(ascending = true).                                        //sort by key: word
      map{                                                                //current shape: RDD[(word, (fileName, count))] . (fileName, count) is an iterable
        case(word, iterable) =>                                           //pattern match
          val vect = iterable.toVector.sortBy{                            //each iterable record is assigned to Vector vect, then perform sort inside the iterable
            case(fileName, count) => (-count, fileName)                   //by mapping, sorted by count descending/biggest num first, then filename
          }
          val (locations, counts) = vect.unzip                            //unzip vect, the value is assigned to locations and counts respectively
          val totalCount = counts.reduceLeft((n1, n2) => n1 + n2)         //reduceleft will reduce the collection down to a final value
          (word, totalCount, locations, counts)                           //reshape RDD to RDD[(word, totalCount, locations, counts)  ]
      }

    val iiDF = spark.createDataFrame(ii).toDF("word", "total_count", "locations", "counts") //create a DataFrame

    //iiDF.createOrReplaceTempView("inverted_index")                      //create a temporary view named "inverted_index"

    //val iiSQL = spark.sql("Select word, total_count, locations[0] as top_locations, counts[0] as top_counts From inverted_index")

    //iiSQL.show(numRows = 20, truncate = false)                          //print contents

    import spark.implicits._                                              //so we can use the 'as' method

    val iiDS = iiDF.as[iiRecord]                                          //convert the DataFrame to DataSet, this will result on typed fields using the case class iiRecord

    iiDS.show()                                                           //print contents

  }

  def keep(x: String) = if(stop_words contains x) false else true

  def info(message: String): String = {

    println(message)

    message

  }

  def error(message: String): String = {

    val fullMessage = s"""
                         |**********************************************************************************************
                         |  ERROR:$message
                         |**********************************************************************************************
        """.stripMargin
    println(fullMessage)

    sys.exit()

    fullMessage

  }

}