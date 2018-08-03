package scalaAbstractClass

trait Buffer {
  type T
  val element: T
}

abstract class SeqBuffer extends Buffer{
  type U
  type T <: Seq[U]
  def length = element.length
}

abstract class IntSeqBuffer extends SeqBuffer{
  type U = Int
}


