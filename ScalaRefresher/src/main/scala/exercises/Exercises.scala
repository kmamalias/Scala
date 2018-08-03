package exercises

object Exercises {
  def main(args: Array[String]): Unit = {
    //val days = Seq("Mon","Tue","Wed","Thur","Fri","Sat","Sun")
    //for(d <- days) println(d)

    //println(factorial(5))
    //asteriskTriangle(5)
    //asteriskUpsideDown(5)
    //asteriskSymmetrical(5)
    //decRep
    //fibonacci(14)
    println(binaryGap(1041))
  }

  def factorial(n: Int): Int ={
    def fact(n: Int, accumulator: Int): Int ={
      if(n == 1) accumulator else fact(n-1, n*accumulator)
    }
    fact(n, 1)
  }

  def asteriskTriangle(n: Int): Unit ={
    for(i <- 1 to n){
      for(j <- 1 to i) yield print('*')
      println()
    }
  }

  def asteriskUpsideDown(n: Int): Unit ={
    for(i <- n to 1 by -1){
      for(j <- 1 to i) yield print('*')
      println()
    }
  }

  def asteriskSymmetrical(n: Int): Unit ={
    for(i <- n to 1 by -1){
      for(j <- 1 to n-i) yield print(" ")
      for(j <- 1 to 2*i-1) yield print('*')
      println
    }
  }

  def decRep() ={
    var n = 5
    var result = 0
    while(n>0){
      n = n%10
      result += 1
    }
    println(result)
  }

  def fibonacci(n: Int): Unit ={
    var numA=0
    var numB=1
    while(numA <= n){
      print(numA)
      var numC = numA + numB
      numA=numB
      numB=numC
    }
  }

  def binaryGap(n: Int): Int = {
    var cur = 0
    var count0 = 0
    var str = ""
    //var s = collection.mutable.Map[String, Int]()

    for(i <- n.toBinaryString){
        if(i == '0'){
          count0 += 1
          str += i
        }else{
          //s += (str -> str.length)
          if(str.length > cur) cur = str.length
          count0 = 0
          str = ""
        }
    }

    //println(count0)
    //println(str)
    //println(s)
    //println(cur)
    cur

  }

}
