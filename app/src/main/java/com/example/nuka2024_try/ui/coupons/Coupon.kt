package com.example.nuka2024_try.ui.coupons

data class Coupon(
    val companyName: String,
    val title: String,
    val discount: String,
    val expiration: String,
    var isUsed: Boolean
)
