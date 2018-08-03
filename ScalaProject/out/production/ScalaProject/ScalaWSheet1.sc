import org.apache.spark.sql.SparkSession

val spark = SparkSession.builder.appName("Simple App").master("local[*]").getOrCreate()
val sc = spark.sparkContext

sc.version
