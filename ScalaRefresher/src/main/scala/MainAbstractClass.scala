import scalaAbstractClass.IntSeqBuffer

object MainAbstractClass {
  def main(args: Array[String]): Unit = {
    val buf = newIntSeqBuff(7,8)
    println("length: " + buf.length)
    println("element: " + buf.element)
  }

  def newIntSeqBuff(elem1: Int, elem2: Int): IntSeqBuffer = new IntSeqBuffer{
    type T = List[U]
    val element = List(elem1, elem2)
  }

}
