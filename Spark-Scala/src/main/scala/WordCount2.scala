import java.io.File

import util.FileUtil
import org.apache.spark.sql.SparkSession

object WordCount2 {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder.appName("Word Count 2").config("spark.app.id", "Word Count 2").master("local[*]").getOrCreate()

    val sc = spark.sparkContext

    try{

      //val input_file = sc.textFile("/home/hduser/spark-scala-tutorial/data/kjvdat.txt").map(line => line.toLowerCase)
      val input_file = sc.textFile("/home/kent/Documents/file1.txt").map(line => line.toLowerCase)

      /*val wc = input_file
        .flatMap(line => line.split("""[^\p{IsAlphabetic}]+"""))
        .map(word => (word, 1))
        .reduceByKey((count1, count2) => count1 + count2)
        .sortByKey(ascending = true)

        val out = new File("output/kjv-wc-groupby1")*/

      /*val wc_GroupByFirstLetter = input_file
        .flatMap(line => line.split("""[^\p{IsAlphabetic}]+"""))
        .map(word => (word(0), 1))                               //take the first char of the word:String
        .reduceByKey((count1, count2) => count1 + count2)
        .sortByKey(ascending = true)

        val out = new File("output/kjv-wc-groupby2")*/

      /*val wc_SortByCount = input_file
        .flatMap(line => line.split("""[^\p{IsAlphabetic}]+"""))
        .map(word => (word, 1))
        .reduceByKey((count1, count2) => count1 + count2)
        .sortBy{case (word, count) => (-count, word)}           //sort by counts decending then word ascending

        val out = new File("output/kjv-wc-groupby3")*/

      val wc_GroupByWordCountPair = input_file
        .flatMap(line => line.split("""[^\p{IsAlphabetic}]+"""))
        .map(word => (word, 1))
        .reduceByKey((count1, count2) => count1 + count2)
        .groupBy{case(words, count) => count}                   //group by count
        .sortByKey(ascending = true)

      val out = new File("output/kjv-wc-groupby4")

      //FileUtil.rmrf(out)

      println(s"Writing output to: $out")

      wc_GroupByWordCountPair.saveAsTextFile(out.toString)

    } finally{
      spark.stop()
    }



  }

}
