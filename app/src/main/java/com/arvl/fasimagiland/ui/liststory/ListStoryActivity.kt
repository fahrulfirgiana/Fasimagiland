package com.arvl.fasimagiland.ui.liststory

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arvl.fasimagiland.CanvasActivity
import com.arvl.fasimagiland.Story
import com.arvl.fasimagiland.StoryAdapter
import com.arvl.fasimagiland.databinding.ActivityListStoryBinding

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stories = listOf(
            Story("Jack and the Hidden Boys", "Easy"),
            Story("Lily’s", "Normal"),
            Story("Tiger Adventures in the Wild", "Hard"),
            Story("The Turtle and the Rabbit", "Normal")
        )

        storyAdapter = StoryAdapter(stories)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = storyAdapter

        binding.btnListstory.setOnClickListener {
            val intent = Intent(this, CanvasActivity::class.java)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}
