package scalaPatternMatching

abstract class Device

case class Phone(model: String) extends  Device{
  def screenOff = "Turning screen off"
}

case class Computer(model: String) extends Device{
  def screenSaverOn = "Turning screen saver On"
}


