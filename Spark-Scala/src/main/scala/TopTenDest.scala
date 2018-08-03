import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StructType, StructField, StringType, LongType, TimestampType}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object TopTenDest {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Top Ten Destination")
      .master("local[*]")
      .getOrCreate()

    val csv_input = "/home/kent/IdeaProjects/Spark-Scala/input/clicklog_2017-03-20.csv"

    /*val csv_schema = StructType(
      Array(StructField("user_id", LongType, true),
        StructField("time", TimestampType, true),
        StructField("action", StringType, true),
        StructField("destination", StringType, true),
        StructField("hotel", StringType, true)))*/

    import spark.implicits._

    val input = spark.read.format("CSV")
      .option("header","true")
      .option("infer_schema", "true")
      .option("timestampFormat","hh:mm:ss")
      .load(csv_input)

    val df = input
      .filter("action = 'Search'")
      .groupBy(window($"time", "10 minutes"), $"destination")
      .count()

    df.cache()

    val by_window = Window.partitionBy("window").orderBy('window.asc, 'count.desc)

    val top_ten = df
      .withColumn("rank", row_number() over by_window)
      .sort("window.start", "rank")
      .filter("rank <= 10")

    top_ten.show(truncate=false)
    top_ten.rdd.saveAsTextFile("output/TopTenDest")

    spark.stop()

  }
}
