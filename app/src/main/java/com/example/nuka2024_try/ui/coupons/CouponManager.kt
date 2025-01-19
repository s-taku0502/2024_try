package com.example.nuka2024_try.ui.coupons

object CouponManager {
    private val coupons = mutableListOf<String>() // クーポンリスト

    // クーポンを追加
    fun addCoupon(coupon: String) {
        if (!coupons.contains(coupon)) {
            coupons.add(coupon)
        }
    }

    // 現在のクーポンリストを取得
    fun getCoupons(): List<String> = coupons
}
