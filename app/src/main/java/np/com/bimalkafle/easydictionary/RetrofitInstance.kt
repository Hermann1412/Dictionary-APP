package np.com.bimalkafle.easydictionary

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton object for creating and accessing Retrofit instance
object RetrofitInstance {

    // Base URL for the API
    private const val BASE_URL = "https://wordsapiv1.p.rapidapi.com/"

    // Create and configure the Retrofit instance
    private fun getInstance(): Retrofit {
        // Create a logging interceptor for debugging purposes
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        // Build OkHttpClient with interceptors
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    // Add required headers for the RapidAPI
                    .addHeader("X-RapidAPI-Key", "API's key") //provide by your own
                    .addHeader("X-RapidAPI-Host", "wordsapiv1.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        // Build and return the Retrofit instance
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Create an instance of the Retrofit interface for making API calls
    val dictionaryApi: DictionaryApi = getInstance().create(DictionaryApi::class.java)
}