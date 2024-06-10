package com.arvl.fasimagiland.ui.screen.liststory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arvl.fasimagiland.R
import com.arvl.fasimagiland.databinding.ActivityListStoryBinding
import com.arvl.fasimagiland.model.Story
import com.arvl.fasimagiland.ui.screen.canvas.CanvasActivity
import com.arvl.fasimagiland.ui.screen.liststory.adapter.StoryAdapter

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

        storyAdapter = StoryAdapter(stories) { story ->
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = storyAdapter

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}
