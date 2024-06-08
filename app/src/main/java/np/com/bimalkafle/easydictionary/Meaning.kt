package np.com.bimalkafle.easydictionary


data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String> = emptyList()
)
