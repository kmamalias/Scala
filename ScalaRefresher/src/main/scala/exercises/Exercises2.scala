package exercises

object Exercises2 {
  def main(args: Array[String]): Unit = {
    //val arr = Array(3, 8, 9, 7, 6)
    //val rot = 3
    //val arr = Array(0, 0, 0)
    //val rot = 1
    //val arr = Array(1, 2, 3, 4)
    //val rot = 4
    val arr = Array(0)
    val rot = 5
    //val finalArray = cyclicRotation(arr, rot)
    //finalArray.foreach(print)

    val arr2 = Array(9, 3, 9, 3, 9, 7, 9)
    //val arr2 = Array(1, 2, 3, 1, 2, 1, 3)
    //val arr2 = Array(2)
    println(oddOccurrencesInArray(arr2))

  }

  def cyclicRotation(a: Array[Int], k: Int): Array[Int]={
    var aCopy = scala.collection.mutable.ArrayBuffer[Int]() //Initialize Array
    var temp = scala.collection.mutable.ArrayBuffer.fill(a.length)(0) //Initialize Array with a.length & default zero values
    var counter = 1

    for(i <- a) yield aCopy += i

    while(counter <= k) {
      var tempIndex = 1
      for (i <- 0 until aCopy.length) {
        if (tempIndex == aCopy.length) {
          temp(0) = aCopy(i) //shift value to temp index 0
        } else {
          temp(tempIndex) = aCopy(i) //Shift values
          tempIndex += 1 //increment temp Array index
        }
      }
      aCopy = temp.clone()
      counter += 1
    }
    temp.toArray
  }

  def oddOccurrencesInArray(a: Array[Int]): Int = {
    var x = 0

    for(i <- a){
      var count = 0
      for(j <- a if j == i) yield count += 1
      if(count%2 != 0) x = i
    }

    x
  }
}
