package com.example.nuka2024_try.ui.news

data class NewsItem(
    val id: String,
    val content: String,
    val endDate: String,
    val organization: String,
    val readUsers: List<String>
)
