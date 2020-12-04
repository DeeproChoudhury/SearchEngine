package websearch

class WebCrawler(val url: URL) {
    private val maxPages = 10
    fun run() {
        runhelper(url)
    }

    var storedResults = mutableMapOf<URL, WebPage>()
    fun runhelper(url: URL) {
        try {
            if (storedResults.size < maxPages) {
                val downloaded = url.download()
                storedResults[url] = downloaded
                val extractedLinks = downloaded.extractLinks()
                for (extractedLink in extractedLinks) {
                    if (!storedResults.containsKey(extractedLink)) {
                        runhelper(extractedLink)
                    }
                }
            }
        } catch (e: org.jsoup.HttpStatusException) {
            println("Sorry, there was an error connecting to the webpage!")
        }
    }

    fun dump(): MutableMap<URL, WebPage> {
        return storedResults
    }
}

fun main() {
    val crawler = WebCrawler(url = URL("http://www.bbc.co.uk"))
    crawler.run()
    val searchEngine = SearchEngine(crawler.dump())
    searchEngine.compileIndex()
    println(searchEngine.searchFor("news"))
}
