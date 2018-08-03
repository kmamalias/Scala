import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder

object ScalaTwitterClientExample {
  def main(args: Array[String]): Unit ={
    //1 config work to create a twitter object
    val cb = new ConfigurationBuilder()
      .setDebugEnabled(true)
      .setOAuthConsumerKey("731b1hhiTKbPLcGIA2671WOQb")
      .setOAuthConsumerSecret("7PoIeUqkLe7lgmTh9HIeE7GPsJHqVuKTLlIzuNLVHnLeELLmTz")
      .setOAuthAccessToken("2616422418-wa2GYNMF6hrKAOG5ewAcCrsQFMdT5e6kDWZnRdw")
      .setOAuthAccessTokenSecret("qQGHq9SfNzE4sjLF8kAKAoYE6iD9ff0QokHhp9G5cVEtq")
    val tf = new TwitterFactory(cb.build)
    val twitter = tf.getInstance()

    //2 use the twitter object
    val sn = twitter.getScreenName
    println("Showing my ScreenName" + sn)
    twitter
  }

}
