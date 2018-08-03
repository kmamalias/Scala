import scalaTrait._

import scala.collection.mutable.ArrayBuffer

object MainTrait {
  def main(args: Array[String]): Unit = {
    val top = 10
    val iterator = new IntIterator(top)

    var a = 0
    for(a <- iterator.next to top){
      print(s"$a ")
    }

    val dog = new Dog("Harry")
    val cat = new Cat("Sally")

    val animals = ArrayBuffer.empty[Pets]
    animals.append(dog)
    animals.append(cat)
    animals.foreach(e => println(e.name))
  }

}
