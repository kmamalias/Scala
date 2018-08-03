package scalaTrait

trait Pets{
  val name: String
}

class Cat(val name: String) extends Pets
class Dog(val name: String) extends Pets

object Pet {
}
