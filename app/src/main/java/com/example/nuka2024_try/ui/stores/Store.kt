package com.example.nuka2024_try.ui.stores

import java.net.URL

data class Store(
    val name: String,
    val description: String, // 例：○○の詳細情報
    val industries: String, // 例：業界
    val address: String, // 例：住所
    val website_url: URL, // 例：ホームページURL
    val imageResId: Int, // Drawable リソースID
    var isExpanded: Boolean = false // トグルの状態を保持
)