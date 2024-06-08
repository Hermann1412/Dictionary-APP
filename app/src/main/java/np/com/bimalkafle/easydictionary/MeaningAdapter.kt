package np.com.bimalkafle.easydictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import np.com.bimalkafle.easydictionary.databinding.MeaningRecyclerRowBinding

// Adapter for displaying meanings in a RecyclerView
class MeaningAdapter(private var meaningList: List<Meaning>) : RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder>() {

    // ViewHolder for each meaning item in the RecyclerView
    class MeaningViewHolder(private val binding: MeaningRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind data to the ViewHolder
        fun bind(meaning: Meaning) {
            // Set part of speech
            binding.partOfSpeechTextview.text = meaning.partOfSpeech
            // Set definitions
            binding.definitionsTextview.text = meaning.definitions.joinToString("\n\n") {
                val currentIndex = meaning.definitions.indexOf(it)
                (currentIndex + 1).toString() + ". " + it.definition
            }

            // Show or hide synonyms based on availability
            if (meaning.synonyms.isNotEmpty()) {
                binding.synonymsTitleTextview.visibility = View.VISIBLE
                binding.synonymsTextview.visibility = View.VISIBLE
                binding.synonymsTextview.text = meaning.synonyms.joinToString(", ")
            } else {
                binding.synonymsTitleTextview.visibility = View.GONE
                binding.synonymsTextview.visibility = View.GONE
            }
        }
    }

    // Update the data in the adapter with a new list of meanings
    fun updateNewData(newMeaningList: List<Meaning>) {
        meaningList = newMeaningList
        notifyDataSetChanged()
    }

    // Create a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningViewHolder {
        val binding = MeaningRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeaningViewHolder(binding)
    }

    // Return the total number of items in the adapter
    override fun getItemCount(): Int {
        return meaningList.size
    }

    // Bind data to the ViewHolder at the specified position
    override fun onBindViewHolder(holder: MeaningViewHolder, position: Int) {
        holder.bind(meaningList[position])
    }
}