package scalaSingletonObj

class Email(val username: String, val domainName: String)

object Email {
  def fromString(emailString: String): Option[Email] = {
    emailString.split('@') match{
      case Array(a, b) => Some(new Email(a, b))
      case _ => None
    }
  }
}
