package scalaMixIns

abstract class AbsIterator{
  type T
  def hasNext: Boolean
  def next(): T
}

class StringIterator(s: String) extends AbsIterator {
  type T = Char

  private var i = 0

  def hasNext = i < s.length

  def next() = {
    val ch = s charAt i
    i += 1
    ch
  }
}
