import java.util.Calendar

object RegExExample {

  def main(args: Array[String]): Unit = {

    val numPattern = "[0-9]+".r             /*The .r creates a RegEx object
                                          * Another option is:
                                          * import scala.util.matching.Regex
                                          * val numPattern = new Regex("[0-9]+")
                                          */
    val address = "123 Main Street Suite 101"

    val datePattern = raw"(\d{4})-(\d{2})-(\d{2})".r
    val datesStr = "Important dates in history: 2004-01-20, 1958-09-05, 2010-10-06, 2011-07-15"

    //To view value:
    println("================================")
    val match1 = numPattern.findFirstIn(address)    //This returns an Option/Some/None pattern

    println("findFirstIn")
    println("Print value with: match1 match { case }")
    match1 match{
      case Some(s) => println(s)
      case None =>
    }
    //or
    println("Print value with: match1.foreach(println)")
    match1.foreach(println)
    //or
    println("Print value with: match1.foreach{ operation }")
    match1.foreach{ e =>
      println(e)
    }
    //or
    println("Print value with: get")
    println(match1.getOrElse("No Match"))

    println("================================")
    val matchAll = numPattern.findAllIn(address)   //This returns an Iterator
    println("findAllIn")
    println("Converting the Iterator to Array and using deep method to return contents of the array")
    val matches = matchAll.toArray
    println(matches.deep.mkString("\n"))
    //or
    //println("Print value with by looping over the iterator: ")
    //matchAll.foreach(println)

    println("================================")
    println("findFirstIn")
    val firstDateA = datePattern.findFirstIn(datesStr)
    println(firstDateA.getOrElse("No Date Found"))
    println("findFirstMatchIn")
    val firstDateB = datePattern.findFirstMatchIn(datesStr)
    for(m <- firstDateB) yield println(m.group(1))    //date only

    println("================================")
    println("findAllMatchIn")
    val allDate = datePattern.findAllMatchIn(datesStr)
    allDate.foreach(println)

    println("================================")
    println("replaceAllIn")
    val redacted = datePattern.replaceAllIn(datesStr, "XXXX-XX-XX")
    println(redacted)

    val yearsOnly = datePattern.replaceAllIn(datesStr, m => m.group(1))
    println(yearsOnly)
    val months      = (0 to 11).map { i => val c = Calendar.getInstance; c.set(2014, i, 1); f"$c%tb" }
    println(months)
    val reformatted = datePattern.replaceAllIn(datesStr, _ match { case datePattern(y,m,d) => f"${months(m.toInt - 1)} $d, $y" })
    println(reformatted)

    val docSpree = """2011(?:-\d{2}){2}""".r
    val docView  = datePattern.replaceAllIn(datesStr, _ match {
      case docSpree() => "Historic doc spree!"
      case _          => "Something else happened"
    })

    println(docView)

  }

}
