package np.com.bimalkafle.easydictionary


data class WordsApiResponse(
    val word: String,
    val results: List<Definition>
) {
    data class Definition(
        val definition: String,
        val partOfSpeech: String,
        val synonyms: List<String> = emptyList()
    )
}
