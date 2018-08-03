import scalaUpperTypeBounds.{Cat, Dog, PetContainer}

object MainUpperTypeBounds {
  def main(args: Array[String]): Unit = {
    //The class PetContainer can only have classes of subtype Pet, so only Dog and Cat.
    //If you instantiate Lion, it will return an error
    val dogContainer = new PetContainer[Dog](new Dog)
    val catContainer = new PetContainer[Cat](new Cat)

    println(dogContainer.pet.name)
  }
}
