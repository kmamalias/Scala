import java.io.File

import org.apache.spark.sql.SparkSession

object Main {

  def main(args: Array[String]): Unit ={

    val input = new File("/home/kent/IdeaProjects/FinArchive/input/DAT File 2/")
    val pathSep = File.separator
    var recCount = 0
    val t = input.listFiles().foreach(_.toString.split(pathSep).last)
    //val t = input.listFiles().toSeq.foreach(_.toString.split(pathSep).last)



    /*
    val t1 = for{
      fname <- t
      fname = fname.toString.split(pathSep)
    } yield { s"Test" }

    println(recCount)
*/
    //val t1 = t.foreach(Map())

    /*
    val spark = SparkSession
      .builder()
      .appName("Fin Archive")
      .master("local[*]")
      .getOrCreate()

    val temp = spark
      .read
      .format("CSV")
      .option("delimiter","|")
      .option("infer_schema","true")
      .load(input)

    temp.show(truncate=false)

    spark.stop()
    */
  }

}
