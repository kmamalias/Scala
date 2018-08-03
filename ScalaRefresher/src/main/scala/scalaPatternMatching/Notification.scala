package scalaPatternMatching

abstract class Communication

case class Email(sender: String, title: String, body: String) extends Communication

case class SMS(caller: String, message: String) extends Communication

case class VoiceRecording(contactName: String, link: String) extends Communication

object Notification {
  def showNotification(notification: Communication): String = {
    notification match{
      case Email(email, title, _) => s"You got an email from $email with title:$title"
      case SMS(number, message) => s"You got an SMS from $number! Message: $message"
      case VoiceRecording(name, link) => s"You received a voice recording from $name! Click the link to hear it: $link"
    }
  }

  def showImportantNotification(notification: Communication, importantPeopleInfo: Seq[String]): String = {
    notification match{
      case Email(email, _, _) if importantPeopleInfo.contains(email) => "You have email from special someone!"
      case SMS(number, _) if importantPeopleInfo.contains(number) => "Someone important sent you an SMS"
      case other => showNotification(other)
    }
  }
}
