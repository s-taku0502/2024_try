package com.example.nuka2024_try.ui.qr_scanner

import com.example.nuka2024_try.ui.coupons.CouponManager

object StampsManager {
    private val stamps = mutableListOf<String>() // スタンプリスト

    // スタンプを追加する
    fun addStamp(stamp: String) {
        stamps.add(stamp)
        checkAndAddCoupons()
    }

    // スタンプ数を取得
    fun getStampCount(): Int = stamps.size

    // クーポン条件をチェックし追加
    private fun checkAndAddCoupons() {
        val count = getStampCount()
        when (count) {
            3 -> CouponManager.addCoupon("10%割引クーポン")
            5 -> CouponManager.addCoupon("15%割引クーポン")
            7 -> CouponManager.addCoupon("20%割引クーポン")
        }
    }
}
