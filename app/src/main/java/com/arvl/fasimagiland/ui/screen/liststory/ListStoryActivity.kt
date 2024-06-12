package com.arvl.fasimagiland.ui.screen.liststory

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arvl.fasimagiland.R
import com.arvl.fasimagiland.data.response.ListStoryResponse
import com.arvl.fasimagiland.data.retrofit.ApiConfig
import com.arvl.fasimagiland.databinding.ActivityListStoryBinding
import com.arvl.fasimagiland.model.Story
import com.arvl.fasimagiland.ui.screen.liststory.adapter.StoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoryAdapter(emptyList()) { story ->
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_up, R.anim.no_animation)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = storyAdapter

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        fetchStories()
    }

    private fun fetchStories() {
        val client = ApiConfig.getApiService().getListStory()
        client.enqueue(object : Callback<List<ListStoryResponse>> {
            override fun onResponse(call: Call<List<ListStoryResponse>>, response: Response<List<ListStoryResponse>>) {
                if (response.isSuccessful) {
                    val stories = response.body()?.map {
                        Story(it.title, it.difficulty)
                    } ?: emptyList()
                    storyAdapter.updateStories(stories)
                } else {
                    Toast.makeText(this@ListStoryActivity, "Failed to fetch stories", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ListStoryResponse>>, t: Throwable) {
                Toast.makeText(this@ListStoryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
