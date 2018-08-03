import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Intro1 {

  def main(args: Array[String]): Unit ={

    val spark = SparkSession.builder.appName("Simple App").config("spark.app.id", "Simple App").master("local[*]").getOrCreate()

    val sc = spark.sparkContext

    val input_file = sc.textFile("/home/hduser/spark-scala-tutorial/data/kjvdat.txt").map(line => line.toLowerCase) //creates an RDD[String] format for each line in the file

    //val sins = input_file.filter(line => line.contains("sin"))    //sample to filter line records where the word "%sin%" exist

    //val filterFunc: String => Boolean = (s:String) => s.contains("god") || s.contains("christ")   //sample filter function to filter line records where the word "%god%" or "%christ%" exist

    //val sinsPlusGodChrist = sins.filter(filterFunc)     //sample line to aply the filter by calling a filter function

    val words = input_file.flatMap(line => line.split("""[^\p{IsAlphabetic}]+"""))    //creates RDD[String] format, tokenizes each line to words

    val wordGroups = words.groupBy(word => word)      //creates RDD[(String, Iterable[String])], where String is the word, and Iterable is an array for each occurence of the word

    val wordCounts1 = wordGroups.map{case(word, iterable_word_group) => (word, iterable_word_group.size)} //creates an RDD[(String, Int)], where String is the word and Int is the size of the array
    peek(wordCounts1)             //calls the peek function to display records

    //or
    //val wordCounts2 = wordGroups.mapValues(group => group.size)   //another way to shape the RDD, since we are not modifying the key, we can use the values to return the size
    //peek(wordCounts2)

    wordCounts1.saveAsTextFile("output/kjv-wc_groupby")

    spark.stop()

  }

  def peek(rdd: RDD[_], n: Int = 10): Unit ={

    println("================")
    rdd.take(n).foreach(println)
    println("================")

  }

}
