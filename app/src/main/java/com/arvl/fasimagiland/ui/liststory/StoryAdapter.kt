package com.arvl.fasimagiland

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arvl.fasimagiland.databinding.ItemStoryBinding

data class Story(val title: String, val difficulty: String)

class StoryAdapter(private val stories: List<Story>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvStoryTitle.text = story.title
            binding.tvStoryDifficulty.text = "Difficulty: ${story.difficulty}"
        }
    }
}
