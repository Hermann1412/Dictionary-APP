package np.com.bimalkafle.easydictionary

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import np.com.bimalkafle.easydictionary.databinding.ActivityMainBinding
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MeaningAdapter
    private val searchHistory = mutableListOf<String>()
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        adapter = MeaningAdapter(emptyList())
        binding.meaningRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.meaningRecyclerView.adapter = adapter

        // Initialize search history RecyclerView
        searchHistoryAdapter = SearchHistoryAdapter(searchHistory)
        binding.searchHistoryList.adapter = searchHistoryAdapter

        // Load search history from persistent storage
        loadSearchHistory()

        // Set click listener for search button
        binding.searchBtn.setOnClickListener {
            val word = binding.searchInput.text.toString()
            if (word.isNotEmpty()) {
                // Add searched word to search history
                addToSearchHistory(word)
                // Perform search
                getMeaning(word)
            } else {
                Toast.makeText(this, "can you enter a word?", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getMeaning(word: String) {
        setInProgress(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<WordsApiResponse> = RetrofitInstance.dictionaryApi.getMeaning(word)
                withContext(Dispatchers.Main) {
                    setInProgress(false)
                    if (response.isSuccessful && response.body() != null) {
                        setUI(response.body()!!)
                    } else {
                        throw Exception("Response wasn't successful: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching meaning", e)
                withContext(Dispatchers.Main) {
                    setInProgress(false)
                    Toast.makeText(applicationContext, "Something is not right: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUI(response: WordsApiResponse) {
        binding.wordTextview.text = response.word

        val meanings = response.results.map { definition ->
            Meaning(
                partOfSpeech = definition.partOfSpeech,
                definitions = listOf(Definition(definition.definition)),
                synonyms = definition.synonyms ?: emptyList() // Ensure synonyms is not null
            )
        }
        adapter.updateNewData(meanings)
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.searchBtn.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.searchBtn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun addToSearchHistory(word: String) {
        searchHistory.add(0, word) // Add the word to the top of the list
        saveSearchHistory()
        updateSearchHistoryAdapter()
    }

    private fun saveSearchHistory() {
        val searchHistoryJson = GsonUtils.toJson(searchHistory)
        val sharedPref = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("history", searchHistoryJson)
        editor.apply()
    }

    private fun loadSearchHistory() {
        val sharedPref = getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        val searchHistoryJson = sharedPref.getString("history", null)
        searchHistory.clear()
        searchHistoryJson?.let {
            val historyList: List<String> = GsonUtils.fromJson(it)
            searchHistory.addAll(historyList)
        }
        updateSearchHistoryAdapter()
    }

    private fun updateSearchHistoryAdapter() {
        searchHistoryAdapter.notifyDataSetChanged()
    }
}