package np.com.bimalkafle.easydictionary

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("words/{word}")
    suspend fun getMeaning(@Path("word") word: String): Response<WordsApiResponse>
}
