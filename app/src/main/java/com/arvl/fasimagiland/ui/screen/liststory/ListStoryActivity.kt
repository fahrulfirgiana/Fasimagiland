package com.arvl.fasimagiland.ui.screen.liststory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.arvl.fasimagiland.databinding.ActivityListStoryBinding
import com.arvl.fasimagiland.ui.screen.canvas.CanvasActivity

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var storyAdapter: StoryAdapter
    private val storyViewModel: StoryViewModel by viewModels()

    // Variable to store selected story ID
    private var selectedStoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()

        storyViewModel.fetchStories()

        binding.ivBack.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter { story ->
            // Store selected story ID
            selectedStoryId = story.id ?: ""
            // Call fetchDetailStory when item is clicked
            storyViewModel.fetchDetailStory(selectedStoryId!!)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListStoryActivity)
            adapter = storyAdapter
        }
    }

    private fun setupObservers() {
        storyViewModel.stories.observe(this, Observer { stories ->
            stories?.let {
                storyAdapter.submitList(it)
                binding.progressBar.visibility = View.GONE // Hide ProgressBar when data is loaded
            }
        })

        storyViewModel.detailStory.observe(this, Observer { detailStory ->
            // Check if detailStory is not empty or null
            if (!detailStory.isNullOrBlank()) {
                if (selectedStoryId != null) {
                    // Start CanvasActivity and pass story text and story ID
                    val intent = Intent(this, CanvasActivity::class.java).apply {
                        putExtra("storyText", detailStory)
                        putExtra("storyId", selectedStoryId)
                    }
                    startActivity(intent)
                }
            }
        })
    }
}


