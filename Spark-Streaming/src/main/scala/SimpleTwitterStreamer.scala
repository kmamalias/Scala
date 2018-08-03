import web.TwitterStream
import twitter4j._

object SimpleTwitterStreamer {

  def main(args: Array[String]): Unit ={
    val twitterStream = TwitterStream.getStream
    twitterStream.addListener(TwitterStream.statusListener)

    val filtersample = new FilterQuery().track("World Cup")
    twitterStream.filter(filtersample)
    Thread.sleep(30)
    twitterStream.cleanUp()
    twitterStream.shutdown()
  }

}
