import scala.util.control._

object Training {
  def main(args: Array[String]): Unit ={
    /*
    * Write a function:
      object Solution { def solution(a: Array[Int]): Int }
      that, given an array A of N integers, returns the smallest positive integer (greater than 0) that does not occur in A.
      For example, given A = [1, 3, 6, 4, 1, 2], the function should return 5.
      Given A = [1, 2, 3], the function should return 4.
      Given A = [−1, −3], the function should return 1.
      Assume that:
              N is an integer within the range [1..100,000];
              each element of array A is an integer within the range [−1,000,000..1,000,000].
      Complexity:
              expected worst-case time complexity is O(N);
              expected worst-case space complexity is O(N) (not counting the storage required for input arguments).
    * */

    val a1 = Array(1, 2, 3, 5, -1) //answer should be 4
    val a2 = Array(-1, -2, -3, -5, -1, 1)  //answer should be 2
    val a3 = Array(-1, -3) //answer should be 1
    val a4 = Array(1)  //answer should be 2
    val a5 = Array(5)  //answer should be 1
    val a6 = Array(0)  //answer should be 1
    val a7 = Array(3)  //answer should be 1
    val a8 = Array(-2, 4, 5, 6, 2)  //answer should be 1
    val a9 = Array(10)  //answer should be 1

    println(solution(a1))
    println(solution(a2))
    println(solution(a3))
    println(solution(a4))
    println(solution(a5))
    println(solution(a6))
    println(solution(a7))
    println(solution(a8))
    println(solution(a9))

  }

  def solution(a: Array[Int]): Int ={
    var missing = 1
    var b = a.filter(num => num>0).sorted
    val found = new Breaks

    found.breakable {
      for (i <- 1 to 100000) {
        if (!b.contains(i)) {
          missing = i
          found.break
        }
      }
    }

    missing
  }

}
