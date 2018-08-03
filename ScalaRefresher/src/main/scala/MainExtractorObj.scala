import scalaExtractorObj.CustomerId

object MainExtractorObj {
  def main(args: Array[String]): Unit = {
    val cust1 = CustomerId("Kent") //is equivalent to CustomerId.apply("Kent")
    cust1 match{
      case CustomerId(name) => println(name)
      case _ => println("Could not extract a customer Id")
    }

    val cust2 = CustomerId.apply("--afdsafdsa")

  }

}
