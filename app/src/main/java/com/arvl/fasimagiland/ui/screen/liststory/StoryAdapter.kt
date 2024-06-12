package com.arvl.fasimagiland.ui.screen.liststory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arvl.fasimagiland.databinding.ItemStoryBinding
import com.arvl.fasimagiland.model.Story

class StoryAdapter(
    private var stories: List<Story>,
    private val onClick: (Story) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    fun updateStories(newStories: List<Story>) {
        stories = newStories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
        holder.itemView.setOnClickListener { onClick(story) }
    }

    override fun getItemCount(): Int = stories.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.tvStoryTitle.text = story.title
            binding.tvStoryDifficulty.text = story.difficulty
        }
    }
}
