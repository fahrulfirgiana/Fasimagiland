package com.arvl.fasimagiland.ui.screen.liststory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arvl.fasimagiland.databinding.ItemStoryBinding
import com.arvl.fasimagiland.model.Story

class StoryAdapter(
    private val stories: List<Story>,
    private val onItemClick: (Story) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvStoryTitle.text = story.title
            binding.tvStoryDifficulty.text = story.difficulty

            binding.root.setOnClickListener {
                onItemClick(story)
            }
        }
    }
}
