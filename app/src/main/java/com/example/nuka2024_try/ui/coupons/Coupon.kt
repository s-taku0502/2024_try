package com.example.nuka2024_try.ui.coupons

import java.util.Date

data class Coupon(
    val id: String = "",              // FirestoreのドキュメントID
    val storeName: String = "",       // 店舗名 (新規追加)
    val title: String = "",           // クーポンのタイトル
    val discount: String = "",        // 割引内容
    val limit: Date? = null,          // 有効期限
    var isUsed: Boolean = false       // 使用済みかどうか
)
