object MainForComprehension {

  case class User(name: String, age: Int)

  def main(args: Array[String]): Unit = {
    val userBase = List(
      new User("Kent", 30),
      new User("Mamalias", 29),
      new User("UNK", 26),
      new User("unk", 48)
    )

    val twentySomethings: List[String] = for(user <- userBase if(user.age>=20 && user.age<30)) yield user.name

    twentySomethings.foreach(name => println(name))

    println("---------------")

    //Given pair(a, b), calculate every instance of number starting from 0 that would sum to b, foo is the method below
    fooA(10, 10) foreach{
      case(i, j) => println(s"$i, $j")
    }

    println("---------------")

    //Same scenario but omit 'yield' in fooB
    fooB(10,10)
  }

  def fooA(numA: Int, numB: Int) =
    for(a <- 0 until numA;
        b <- a until numA if a+b == numB)
      yield (a, b)

  def fooB(numA: Int, numB: Int) =
    for(a <- 0 until numA;
        b <- a until numA if a+b == numB)
      println(s"$a, $b")

}
