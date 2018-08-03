import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._

object Local_SparkTutorial {

  // $example on:create_ds$
  case class Person(name: String, age: Long)

  def main(args: Array[String]): Unit ={

    //Spark Session
    val spark = SparkSession.builder.appName("Simple App").master("local[*]").getOrCreate()
    val sc = spark.sparkContext

    //runBasicDataFrameExample(spark)
    //runCreateDataSetExample(spark)
    //runInferSchemaExample(spark)
    runProgrammaticSchemaExample(spark)

    spark.stop()

  }

  private def runBasicDataFrameExample(spark: SparkSession): Unit = {
    //Create DataFrame
    val df = spark.read.json("/usr/local/spark/examples/src/main/resources/people.json")
    /*
    //Display content of DataFrame to stdout
    df.show()

    //Print schema
    df.printSchema()

    //Select only the "name" column
    df.select("name").show()

    //This import is needed to use the $-notation
    import spark.implicits._
    //Select all but increment age by 1
    df.select($"name", $"age" + 1).show()

    //Select filter
    df.filter($"age" > 21).show()

    //Count People by age
    df.groupBy("age").count().show()
    */
    //Register the DataFrame as a SQL temporary view
    df.createOrReplaceTempView("people")

    val sqlDF = spark.sql("Select * From people")
    sqlDF.show()
  }

  private def runCreateDataSetExample(spark: SparkSession): Unit = {
    //This import is needed to use the $-notation
    import spark.implicits._

    //create Class Person as DataSet
    val caseClassDS = Seq(Person("Andy", 32), Person("Kent", 30)).toDS()
    caseClassDS.show()

    //another example
    val primitiveDS = Seq(1, 2, 3).toDS()
    primitiveDS.map(_ + 1).collect().foreach(print(_))

    //DataFrames can be converted to dataset by providing a class. Mapping will be done by name
    val path = "/usr/local/spark/examples/src/main/resources/people.json"
    val peopleDS = spark.read.json(path).as[Person]
    peopleDS.show()
  }

  private def runInferSchemaExample(spark: SparkSession): Unit = {
    // For implicit conversions from RDDs to DataFrames
    import spark.implicits._

    //Create RDD of Person objects from a text file, convert to DataFrame
    val peopleDF = spark.sparkContext
      .textFile("/usr/local/spark/examples/src/main/resources/people.txt")
      .map(_.split(","))
      .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
      .toDF()

    //Register the DataFrame as a temporary view
    peopleDF.createOrReplaceTempView("people")

    //sql statement
    val teenagersDF = spark.sql("Select name, age From people Where age between 13 and 19")
    teenagersDF.show()

    //Select only the name column. Can be accessed by field index
    teenagersDF.map(teenager => "Name: " + teenager(0)).show()

    //or by field name
    teenagersDF.map(teenager => "Name: " + teenager.getAs[String]("name")).show()
  }

  private def runProgrammaticSchemaExample(spark: SparkSession): Unit = {
    import spark.implicits._
    //Create an RDD
    val peopleRDD = spark.sparkContext.textFile("/usr/local/spark/examples/src/main/resources/people.txt")

    //The schema is encoded in a String
    val schemaString = "name age"

    //Generate the schema based on the String of schema
    val fields = schemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))
    val schema = StructType(fields)

    //Convert records of the RDD(peopleRDD) to rows
    val rowRDD = peopleRDD
      .map(_.split(","))
      .map(attributes => Row(attributes(0), attributes(1).trim))

    //Apply schema to the RDD
    val peopleDF = spark.createDataFrame(rowRDD, schema)

    //Create a temporary view using the dataframe
    peopleDF.createOrReplaceTempView("people")

    //Select SQL
    val results = spark.sql("Select * From people")

    //show name only
    //results.map(attributes => "Name: " + attributes(0)).show()

    //show name and age
    //results.map(attributes => ("Name: " + attributes(0), "Age: " + attributes(1))).show()

    //show name and age
    results.show()

  }
}
