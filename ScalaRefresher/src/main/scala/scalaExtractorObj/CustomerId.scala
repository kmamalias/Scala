package scalaExtractorObj

import scala.util.Random

object CustomerId {
  def apply(name: String): String = s"$name--${Random.nextLong()}"

  def unapply(customerId: String): Option[String] = {
    val stringArray: Array[String] = customerId.split("--")
    if(stringArray.tail.nonEmpty) Some(stringArray.head) else None
  }

}
