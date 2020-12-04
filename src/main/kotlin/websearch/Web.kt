package websearch
import org.jsoup.HttpStatusException
import org.jsoup.nodes.Document
import org.jsoup.Jsoup.connect

data class URL (val url: String) {
    override fun toString(): String = url

    fun download():WebPage {
            val downloaded = connect(url).get()
            return WebPage(downloaded)

    }

}

class WebPage (private val webContent: Document) {
    fun extractWords(): List<String> {
        return webContent.text().split(" ").map{s-> s.replace(".", "").replace(",", "")}. map {it.toLowerCase()}
    }

    fun extractLinks(): List<URL> {
        val aTags = webContent.getElementsByTag("a")
        var link = mutableListOf<URL>()
        for (aTag in aTags) {
            var urlString = aTag.attr("href")
            if (urlString.startsWith("https://") || urlString.startsWith("http://")) {
                link.add(URL(urlString))
            }
        }
        return link
    }
}