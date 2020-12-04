package websearch

class SearchEngine (val corpus: Map<URL, WebPage>) {
    private var index = mutableMapOf<String, List<SearchResult>>()

    fun compileIndex(){
        val pairurl = mutableListOf<Pair<String,URL>>()
        val results :Map<URL,List<String>> = corpus.mapValues { entry -> entry.value.extractWords() }
        for (result in results) {
            val word = result.value
            for (w in word) {
                pairurl.add(Pair(w, result.key))
            }
        }

        var grouped = pairurl.groupBy({s -> s.first}, {p -> p.second} )

        fun rank(listurl: List<URL>): List<SearchResult> {
            val groupedURL = listurl.groupBy{s -> s}
            val listURLSize = groupedURL.mapValues {(s, t) ->  t.size }
            val searchresults = listURLSize.map{(s, t) -> SearchResult(s, t)}
            return searchresults
        }

        val newGrouped = grouped.mapValues{ (r, s) -> rank(s) }

        index.putAll(newGrouped)

    }

    fun searchFor(key: String) = SearchResultsSummary(key, index!![key]!!)
}

class SearchResult(val url: URL, val numRefs: Int) {

}

class SearchResultsSummary(val query: String, val results: List<SearchResult>){
    override fun toString(): String {
        val printresults = results.map({ s ->  "$s\n"})

        val queryStatement = "results for $query\n $printresults"
        return queryStatement
    }

}