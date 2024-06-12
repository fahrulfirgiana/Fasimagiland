package com.arvl.fasimagiland.data.response

import com.google.gson.annotations.SerializedName

data class DetailStoryResponse(

	@field:SerializedName("DetailStoryResponse")
	val detailStoryResponse: List<DetailStoryResponseItem>
)

data class SegmentsItem(

	@field:SerializedName("segmentNumber")
	val segmentNumber: Int,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("highlightedWord")
	val highlightedWord: String
)

data class DetailStoryResponseItem(

	@field:SerializedName("storyId")
	val storyId: String,

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("countSegments")
	val countSegments: Int,

	@field:SerializedName("segments")
	val segments: List<SegmentsItem>
)
