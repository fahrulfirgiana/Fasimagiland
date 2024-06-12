package com.arvl.fasimagiland.data.retrofit
import com.arvl.fasimagiland.data.response.DetailStoryResponseItem
import com.arvl.fasimagiland.data.response.ListStoryResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    fun getListStory(): Call<List<ListStoryResponse>>

    @GET("detail/{id}/story")
    fun getDetailStory(
        @Path("id") id: String
    ): Call<DetailStoryResponseItem>
}