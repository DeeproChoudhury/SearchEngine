package websearch

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.Test
import kotlin.test.assertEquals

class WebPageTest {

    @Test
    fun `extracts words from page`() {

        val html =
            """
            <html>
              <body>
                <p>this is a simple document</p>
              </body>
            </html>"""

        val htmlDocument: Document = Jsoup.parse(html)

        val webPage = WebPage(htmlDocument)
        assertEquals(listOf("this", "is", "a", "simple", "document"), webPage.extractWords())
    }

    @Test
    fun `converts case and removes punctuation`() {

        val html =
            """
            <html>
              <head>
                <title>Simple Page</title>
              </head>
              <body>
                <p>This is a very, very simple <a href="https://en.wikipedia.org/wiki/HTML">HTML</a> document.</p>
              </body>
            </html>"""

        val htmlDocument: Document = Jsoup.parse(html)

        val webPage = WebPage(htmlDocument)
        assertEquals(
            listOf("simple", "page", "this", "is", "a", "very", "very", "simple", "html", "document"),
            webPage.extractWords()
        )
    }

    /*** For the extension... ***/

    @Test
    fun `extracts links from page`() {

        val html =
            """
            <html>
              <head>
                <title>Simple Page</title>
              </head>
              <body>
                <p>This is a simple <a href="https://en.wikipedia.org/wiki/HTML">HTML</a> document.</p>
                <p>But it has two <a href="https://www.w3schools.com/html/html_links.asp">links</a>.</p>
                <p>
              </body>
            </html>"""

        val htmlDocument: Document = Jsoup.parse(html)

        val webPage = WebPage(htmlDocument)
        assertEquals(
            listOf(
                URL("https://en.wikipedia.org/wiki/HTML"),
                URL("https://www.w3schools.com/html/html_links.asp")
            ),
            webPage.extractLinks()
        )
    }

    @Test
    fun `does not extract links without http or https`() {
        val html =
            """
            <html>
              <head>
                <title>Simple Page</title>
              </head>
              <body>
                <p>This is a simple <a href="https://en.wikipedia.org/wiki/HTML">HTML</a> document.</p>
                <p>But it has four <a href="https://www.w3schools.com/html/html_links.asp">links</a>.</p>
                <p>One of them <a href="ww1.piratedwebsite.com/illegalstuff"> does not contain http or https</a>.</p>
                <p>And another <a href="www.https.com/http"> contains https and http but in the wrong place</a>.</p>
              </body>
            </html>"""

        val htmlDocument: Document = Jsoup.parse(html)

        val webPage = WebPage(htmlDocument)
        assertEquals(
            listOf(
                URL("https://en.wikipedia.org/wiki/HTML"),
                URL("https://www.w3schools.com/html/html_links.asp")
            ),
            webPage.extractLinks()
        )
    }
}
