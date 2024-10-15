package com.example.nuka2024_try.ui.stores

import com.bumptech.glide.load.model.GlideUrl
import java.net.URL

// Storeデータクラスを定義
data class Store(
    val imageUrl: GlideUrl,  // 画像のリソースID
    val name: String      // 店舗名
)