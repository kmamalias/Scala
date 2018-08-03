import java.io.File

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{min, row_number, window}

object LoadClicklog {

  def main(args: Array[String]): Unit ={

    //val warehouseLocation = new File("hdfs://localhost:50070/user/hive/warehouse").getAbsolutePath

    val spark = SparkSession
      .builder
      .appName("Load Clicklog")
      .master("local[*]")
      //.config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    //val input_dir = new File("file:/home/kent/IdeaProjects/Spark-Scala/input/input_2/*")

    try{

      spark.catalog.listDatabases.show(false)
      spark.catalog.listTables.show(false)
      spark.conf.getAll.mkString("\n")
      //spark.sql("Show tables").show
      //process_csv(spark, input_dir)
      //process_toptendest(spark)
      //process_secondclickedhotel(spark)

    }finally{

      spark.stop()

    }
  }

  def process_csv(spark: SparkSession, input_dir: File): Unit ={

    /*
      load csv file into df,
      drop duplicate records in case directory contains duplicate files
    */
    val df_clicklog = spark
      .read
      .format("CSV")
      .option("header","true")
      .option("infer_schema","true")
      .option("timestampFormat","hh:mm:ss")
      .load(input_dir.toString)
      .dropDuplicates

    df_clicklog.createOrReplaceTempView("temp_clicklog")

    println("Creating Table stg_clicklog if not yet existing:")
    //creat_tbl_stgclicklog(spark)
    spark.sql("CREATE TABLE IF NOT EXISTS stg_clicklog(user_id BIGINT, time TIMESTAMP, action STRING, destination STRING, hotel STRING)")

    println("Inserting data into stg_clicklog:")
    spark.sql("INSERT OVERWRITE TABLE stg_clicklog SELECT * FROM temp_clicklog")

    spark.sql("Select * from stg_clicklog").show

  }

  def process_toptendest(spark: SparkSession): Unit ={

    import spark.implicits._

    /*time bucket, ordered by time, count*/
    val by_time = Window
      .partitionBy("window")
      .orderBy('window.asc, 'count.desc)

    /*retrieve ten most searched destinations for every 10min interval*/
    val df_search = spark.sql("Select * From stg_clicklog Where action = 'Search'")
      .groupBy(window($"time", "10 minutes"), $"destination")
      .count()
      .withColumn("rank", row_number() over by_time)
      .sort("window.start", "rank")
      .filter("rank <= 10")
      .selectExpr("window.start as time_start", "window.end as time_end", "destination", "rank")

    df_search.cache()
    df_search.createOrReplaceTempView("temp_topten")

    println("Create table toptendest if not yet existing:")
    //create_tbl_toptendest(spark)
    spark.sql("CREATE TABLE IF NOT EXISTS toptendest(time_start TIMESTAMP, time_end TIMESTAMP, destination STRING, rank INT)")

    println("Insert data into toptendest:")
    spark.sql("INSERT OVERWRITE TABLE toptendest SELECT * FROM temp_topten")

  }

  def process_secondclickedhotel(spark: SparkSession): Unit ={

    import spark.implicits._

    /*user_id bucket, ordered by earliest time*/
    val by_userid = Window
      .partitionBy("user_id")
      .orderBy("min_time")

    /*retrieve first & second clicked hotel*/
    val df_click = spark.sql("Select * From stg_clicklog Where action = 'Click'")
      .groupBy("user_id","hotel")
      .agg(min($"time") as "min_time")
      .withColumn("rank", row_number() over by_userid)
      .filter("rank <= 2")

    df_click.cache()
    df_click.createTempView("userclickedhotel")

    /*retrieve second clicked hotel, if the user did not click a second hotel return as null*/
    val second_clicked_hotel = spark
      .sql("Select user_id, max(case when rank = 2 then hotel else null end) as hotel From userclickedhotel Group By user_id Order By user_id")

    second_clicked_hotel.createTempView("temp_second_clicked_hotel")

    println("Create table secondclickedhotel if not yet existing:")
    //create_tbl_secondclickedhotel(spark)
    spark.sql("CREATE TABLE IF NOT EXISTS secondclickedhotel(user_id BIGINT, destination STRING)")

    println("Insert data into secondclickedhotel")
    spark.sql("INSERT OVERWRITE TABLE secondclickedhotel SELECT * FROM temp_second_clicked_hotel")

  }

  /*
  def creat_tbl_stgclicklog(spark: SparkSession): Unit ={

    spark.sql("CREATE TABLE IF NOT EXISTS stg_clicklog(user_id BIGINT, time TIMESTAMP, action STRING, destination STRING, hotel STRING)")

  }

  def create_tbl_toptendest(spark: SparkSession): Unit ={

    spark.sql("CREATE TABLE IF NOT EXISTS toptendest(time_start TIMESTAMP, time_end TIMESTAMP, destination STRING, rank INT)")

  }

  def create_tbl_secondclickedhotel(spark: SparkSession): Unit ={

    spark.sql("CREATE TABLE IF NOT EXISTS secondclickedhotel(user_id BIGINT, destination STRING)")

  }*/

}
