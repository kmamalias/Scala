import scalaGenericClass.Stack

object MainGenericClass {

  class Fruit

  class Apple extends  Fruit

  class Banana extends Fruit

  def main(args: Array[String]): Unit = {

    //This stack only takes in Int types
    val intStack = new Stack[Int]

    intStack.push(1)
    intStack.push(3)
    intStack.push(2)
    println(intStack.pop())
    println(intStack.peek)

    //SubTyping
    val fruitStack = new Stack[Fruit]
    val apple = new Apple
    val banana = new Banana

    fruitStack.push(apple)
    fruitStack.push(banana)
    println(fruitStack.peek)

  }

}
