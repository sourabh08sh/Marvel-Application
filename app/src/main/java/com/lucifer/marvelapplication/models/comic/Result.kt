package com.lucifer.marvelapplication.models.comic

data class Result(
    val description: String,
    val id: Int,
    val thumbnail: Thumbnail,
    val title: String
)