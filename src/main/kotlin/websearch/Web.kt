package websearch
import org.jsoup.nodes.Document

data class URL (val url: String) {
    override fun toString(): String = url

    fun download():WebPage {

    }
}

class WebPage (private val webContent: Document) {
    fun extractWords(): List<String> {
        return webContent.text().split(" ").map{s-> s.replace(".", "").replace(",", "")}. map {it.toLowerCase()}
    }
}