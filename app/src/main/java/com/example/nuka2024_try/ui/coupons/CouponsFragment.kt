package com.example.nuka2024_try.ui.coupons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nuka2024_try.R
import com.example.nuka2024_try.ui.stamps.StampListener

// データクラス：クーポン
data class Coupon(val storeName: String, val details: String, val discount: String)

class CouponsFragment : Fragment(), StampListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var couponAdapter: CouponAdapter
    private val coupons = mutableListOf<Coupon>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coupons, container, false)

        // RecyclerView の初期化
        recyclerView = view.findViewById(R.id.couponsRecyclerView)
        couponAdapter = CouponAdapter(coupons)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = couponAdapter

        return view
    }

    // スタンプ数が更新された際に呼び出される
    override fun onStampCountUpdated(count: Int) {
        when (count) {
            3 -> addCoupon("株式会社〇〇", "やきとり", "10%OFF")
            5 -> addCoupon("株式会社〇〇", "やきとり", "15%OFF")
            7 -> addCoupon("株式会社〇〇", "やきとり", "20%OFF")
        }
    }

    // クーポンをリストに追加
    private fun addCoupon(storeName: String, details: String, discount: String) {
        val newCoupon = Coupon(storeName, details, discount)
        coupons.add(newCoupon)
        couponAdapter.notifyDataSetChanged()
    }
}
