package scalaMixIns

abstract class A{
  val message: String
}

class B extends A{
  val message: String = "I'm an instance of B"
}

trait C extends A{
  def loudMessage: String = message.toUpperCase()
}

class D extends B with C

object scalaMixIns {

}
