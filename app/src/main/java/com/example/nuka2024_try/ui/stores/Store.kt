package com.example.nuka2024_try.ui.stores

data class Store(
    val documentId: String,        // Firestore のドキュメントID（店舗名を利用する場合など）
    val name: String,              // 店舗名
    val description: String,       // 店舗の詳細情報
    val industry: String,          // 業界
    val company_features: String,  // 企業の特徴
    val address: String,           // 住所
    val website_url: String,       // ホームページURL
    val imageUrl: String,          // Firebase Storage に保存した画像のURL
    var isExpanded: Boolean = false  // 詳細表示の開閉状態
)
