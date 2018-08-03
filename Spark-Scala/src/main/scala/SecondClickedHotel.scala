import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{StructType, StructField, StringType, LongType, TimestampType}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

object SecondClickedHotel {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Second Clicked Hotel")
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
      .filter("action = 'Click'")
      .groupBy("user_id","hotel")
      .agg(min($"time") as "min_time")

    df.cache()

    val by_userid = Window
      .partitionBy("user_id")
      .orderBy("min_time")

    val hotel = df
      .withColumn("rank", row_number() over by_userid)
      .filter("rank <= 2")

    hotel.createTempView("userclickedhotel")

    val second_clicked_hotel = spark
      .sql("Select user_id, max(case when rank = 2 then hotel else null end) as hotel From userclickedhotel Group By user_id Order By user_id")

    second_clicked_hotel.show(truncate = false)
    second_clicked_hotel.rdd.saveAsTextFile("output/SecondClickedHotel")

    spark.stop()
  }

}
