package web

import com.typesafe.config.ConfigFactory
import twitter4j._
import twitter4j.conf.{Configuration, ConfigurationBuilder}

object TwitterStream {

  private val conf = ConfigFactory.load()

  private val getTwitterConf: Configuration = {
    val twitterConf = new ConfigurationBuilder()
      .setDebugEnabled(true)
      .setOAuthConsumerKey("731b1hhiTKbPLcGIA2671WOQb")
      .setOAuthConsumerSecret("7PoIeUqkLe7lgmTh9HIeE7GPsJHqVuKTLlIzuNLVHnLeELLmTz")
      .setOAuthAccessToken("2616422418-wa2GYNMF6hrKAOG5ewAcCrsQFMdT5e6kDWZnRdw")
      .setOAuthAccessTokenSecret("qQGHq9SfNzE4sjLF8kAKAoYE6iD9ff0QokHhp9G5cVEtq")
      .build()

    twitterConf
  }

  def getStream = new TwitterStreamFactory(getTwitterConf).getInstance()

  def statusListener = new StatusListener {
    def onStatus(status: Status) { println(status.getText) }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace() }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }


}
