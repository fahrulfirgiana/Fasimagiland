package com.arvl.fasimagiland.data.response

import com.google.gson.annotations.SerializedName

data class ListStoryResponse(

	@field:SerializedName("difficulty")
	val difficulty: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String
)
