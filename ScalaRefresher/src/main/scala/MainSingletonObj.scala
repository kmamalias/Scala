import scalaSingletonObj.{Circle, Email}
import scalaSingletonObj.Logger.info

object MainSingletonObj {

  class Project(name: String, daysToComplete: Int)

  class Test{
    val project1 = new Project("Project 1", 12)
    val project2 = new Project("Project 2", 3)
    info("Create Project")
  }

  def main(args: Array[String]): Unit = {
    //Example 1 - Singleton Object
    val test = new Test()

    //Example 2 - Companion Object
    val circ = new Circle(5.0)
    println(circ)
    println(circ.area)
    println(circ.radius)

    //Example - Companion Object
    val email1 = Email.fromString("kent@gmail.com")
    email1 match{
      case Some(email) => println(s"Registered an email, Username: ${email.username}, Domain: ${email.domainName}")
      case None => println("Could not parse Email")
    }
  }

}
