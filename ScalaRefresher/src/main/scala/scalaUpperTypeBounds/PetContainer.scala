package scalaUpperTypeBounds

abstract class Animal{
  def name: String
}

abstract class Pet extends Animal{}

class Dog extends Pet{
  override def name: String = "Dog"
}

class Cat extends Pet{
  override def name: String = "Cat"
}

class Lion extends Animal{
  override def name: String = "Lion"
}

class PetContainer[P <: Pet](p: P){
  def pet: P = p
}