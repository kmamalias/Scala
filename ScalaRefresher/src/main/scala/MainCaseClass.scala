object MainCaseClass {
  case class Book(isbn: String)

  case class Message(sender: String, recepient: String, body: String)

  def main(args: Array[String]): Unit = {
    val frankenstein = Book("978-0486282114")
    println(frankenstein)
    println(frankenstein.isbn)

    val message1 = Message("kent@test.com", "mamalias@test.com", "this is a test")
    println(message1.sender)

    val message2 = Message("kent@test.com", "mamalias@test.com", "this is another test")
    val message3 = Message("kent@test.com", "mamalias@test.com", "this is another test")
    val messageAreTheSame = message2 == message3
    println(messageAreTheSame)

    val message4 = Message("kent@test.com", "mamalias@test.com", "This is a test")
    val message5 = message4.copy(sender = message4.recepient, recepient = "another@test.com")
    println(message5)
  }

}
