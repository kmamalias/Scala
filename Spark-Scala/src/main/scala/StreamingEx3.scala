import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{OutputMode, Trigger}

object StreamingEx3 {

  def main(args: Array[String]): Unit ={
    val spark = SparkSession
      .builder()
      .master("local")
      .appName("Streaming w Kafka")
      .getOrCreate()

    // Extract
    val records = spark.
      readStream.
      format("kafka").
      option("subscribepattern", """topic\d"""). // <-- topics with a digit at the end
      option("kafka.bootstrap.servers", "localhost:9092").
      option("startingoffsets", "latest").
      option("maxOffsetsPerTrigger", 1).
      load

    import spark.implicits._

    // Transform
    val result = records.
      select(
        $"key" cast "string",   // deserialize keys
        $"value" cast "string", // deserialize values
        $"topic",
        $"partition",
        $"offset")

    // Load
    import scala.concurrent.duration._
    val sq = result.
      writeStream.
      format("console").
      option("truncate", false).
      //trigger(Trigger.ProcessingTime(10.seconds)).
      outputMode(OutputMode.Append).
      queryName("from-kafka-to-console").
      start

    // In the end, stop the streaming query
    sq.stop
  }

}
