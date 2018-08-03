import scalaPatternMatching._
import scalaPatternMatching.Notification._

import scala.util.Random

object MainPatternMatching {
  def main(args: Array[String]): Unit = {

    //Example 1
    val x: Int = Random.nextInt(10)
    x match{
      case 0 => println("zero")
      case 1 => println("one")
      case 2 => println("two")
      case _ => println(s"$x is many")
    }

    //Example 2
    def matchTest(x: Int): String = x match {
      case 0 => "zero"
      case 1 => "one"
      case _ => s"$x is many"
    }
    val y: Int = Random.nextInt(10)
    println(matchTest(y))

    //Example 3 - Matching on case classes and pattern guards, see Notification.scala for class implementation
    val someSMS = SMS("12345", "Are you there?")
    val someVR = VoiceRecording("Tom", "voicerecording.org/id/123")
    println(showNotification(someSMS))
    println(showNotification(someVR))

    val importantPeopleInfo = Seq("867-5309", "kent@gmail.com")
    val someSMS2 = SMS("867-5309", "Are you there?")
    val someVR2 = VoiceRecording("XX", "voicerecording.org/id/124")
    val importantEmail = Email("kent@gmail.com", "Drinks Tonight?", "I'm free after 5!")
    val importantSMS = SMS("867-5309", "Im here. Where are you?")
    println(showImportantNotification(someSMS2, importantPeopleInfo))
    println(showImportantNotification(someVR2, importantPeopleInfo))
    println(showImportantNotification(importantEmail, importantPeopleInfo))
    println(showImportantNotification(importantSMS, importantPeopleInfo))

    //Example 4 - Matching on Type only, see Device.scala for class implementation
    def goIdle(device: Device) = device match{
      case p: Phone => p.screenOff
      case c: Computer => c.screenSaverOn
    }

    val comp1 = Computer("KentPC")
    println(goIdle(comp1))
    val phone1 = Phone("KentPhone")
    println(goIdle(phone1))

    //Example 5 - Sealed Class means all class subtype should be declared in the same file.
    //This assures that all subtypes are known. See Furniture.scala for class implementation
    def findPlaceToSit(piece: Furniture): String = piece match{
      case a: Couch => "Lie on the couch"
      case b: Chair => "Sit on the chair"
    }
    val couch1 = Couch()
    val chair1 = Chair()
    println(findPlaceToSit(couch1))
    println(findPlaceToSit(chair1))
  }

}
