trait User{
  def username: String
}

trait Tweeter{
  this: User => //reassign this
  def tweet(tweetText: String) = println(s"$username: $tweetText")
}

class verifiedTweeter(val username_ : String) extends Tweeter with User{ //mixin User because Tweeter required it
  def username = s"real $username_"
}

object MainScalaSelfType {
  def main(args: Array[String]): Unit = {
    val realBeyonce = new verifiedTweeter("Beyonce")
    realBeyonce.tweet("Just spilled my glass of lemonade")
  }
}
