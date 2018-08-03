object MainHigherOrderFuncURL {

  def urlBuilder(ssl: Boolean, domainName: String): (String, String) => String = {
    var schema = if(ssl) "https://" else "http://"
    (endpoint: String, query: String) => s"$schema$domainName/$endpoint?$query"
  }

  def main(args: Array[String]): Unit = {
    val domainName = "www.example.com"
    def getURL = urlBuilder(ssl=true, domainName)
    val endpoint = "users"
    val query = "id=1"
    val url = getURL(endpoint, query)

    println(url)
  }

}
