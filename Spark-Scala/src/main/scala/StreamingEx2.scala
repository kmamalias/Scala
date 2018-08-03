import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

object StreamingEx2 {

  def main(args: Array[String]): Unit ={

    val csv_input = "/home/kent/IdeaProjects/Spark-Scala/input/input_streaming/words"

    val spark = SparkSession
      .builder()
      .master("local")
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    import spark.implicits._

    val inputschema = new StructType().add("word", "string")

    val lines: DataFrame = spark.readStream
      .format("csv")
      .schema(inputschema)
      .load(csv_input)

    val words = lines.as[String].flatMap(_.split(" "))

    val wordCounts = words.groupBy("value").count()

    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()

    query.awaitTermination()
  }

}
