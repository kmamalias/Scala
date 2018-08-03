package aviraTest

object TestA {
  def main(args: Array[String])={
    //sumString("22,71")
    //println(solution("5,2,3"))

    test()
  }

  def solution(x: String) = x.head

  def sumString(s: String)={
    val temp = s.split(",")

    temp.foreach(e => println(e.reverse))

    //val tot = temp.reduceLeft((n1, n2) => n1 + n2)

    //println(tot)
  }

  def reverse(x: Array[Int]) ={

  }

  def test() ={
    val num1 = "391"
    val num = "24"
    val num2 = leftPad(num, 3, '0')

    var i = num1.length-1
    var j = num2.length-1
    var carry = 0

    for(i <- num1){
      print(s"i = $i ")
      for(j <- num2){
        print(s"j = $j ")
      }
      println
    }
  }

  def leftPad(s: String, len: Int, elem: Char): String ={
    elem.toString * (len - s.length) + s
  }

}
