import scalaMixIns.{AbsIterator, D, StringIterator}

trait RichIterator extends AbsIterator{
  def fe(f: T => Unit): Unit = while(hasNext) f(next())
}

object MainMixins{

  class RichStringIter extends StringIterator("Kent") with RichIterator

  def main(args: Array[String]): Unit = {
    val d = new D
    println(d.message)
    println(d.loudMessage)

    val richStringIter = new RichStringIter
    richStringIter fe println
  }

}
