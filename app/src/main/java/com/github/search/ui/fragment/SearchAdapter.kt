package com.github.search.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.search.R

import com.github.search.databinding.CellLanguageCountBinding
import com.github.search.databinding.CellTotalCountBinding

import com.github.search.model.LanguageCount

class SearchAdapter(
    private var items: List<LanguageCount>,
    private var totalCount: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LANGUAGE_COUNT = 0
        private const val VIEW_TYPE_TOTAL_COUNT = 1
    }

    // Creating view holders based on view types
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_LANGUAGE_COUNT -> {
                val binding = CellLanguageCountBinding.inflate(layoutInflater, parent, false)
                LanguageCountViewHolder(binding)
            }
            VIEW_TYPE_TOTAL_COUNT -> {
                val binding = CellTotalCountBinding.inflate(layoutInflater, parent, false)
                TotalCountViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // Binding data to view holders
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LanguageCountViewHolder -> holder.bind(items[position])
            is TotalCountViewHolder -> holder.bind(totalCount)
        }
    }

    // Total number of items in the adapter
    override fun getItemCount(): Int = items.size + 1

    // Getting the view type of an item based on its position
    override fun getItemViewType(position: Int): Int {
        return if (position < items.size) {
            VIEW_TYPE_LANGUAGE_COUNT
        } else {
            VIEW_TYPE_TOTAL_COUNT
        }
    }

    // Reloading the adapter with new data
    @SuppressLint("NotifyDataSetChanged")
    fun reload(items: List<LanguageCount>, totalCount: Int) {
        this.items = items
        this.totalCount = totalCount
        notifyDataSetChanged()
    }

    // View holder for LanguageCount items
    inner class LanguageCountViewHolder(private val binding: CellLanguageCountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Binding LanguageCount data to the view holder
        fun bind(item: LanguageCount) {
            binding.nameLabel.text = item.formattedString()
            binding.executePendingBindings()
        }
    }

    // View holder for TotalCount item
    inner class TotalCountViewHolder(private val binding: CellTotalCountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Binding total count data to the view holder
        fun bind(totalCount: Int) {
            // Using a string resource with a placeholder for the total count
            val totalCountText = itemView.context.getString(R.string.results_found, totalCount)
            binding.totalCountLabel.text = totalCountText
            binding.executePendingBindings()
        }
    }
}
