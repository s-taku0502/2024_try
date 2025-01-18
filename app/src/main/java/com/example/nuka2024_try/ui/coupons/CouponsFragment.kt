package com.example.nuka2024_try.ui.coupons

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nuka2024_try.R

class CouponsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var couponAdapter: CouponAdapter
    private var stampCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coupons, container, false)
        recyclerView = view.findViewById(R.id.couponsRecyclerView)

        // RecyclerView の設定
        couponAdapter = CouponAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = couponAdapter

        // スタンプデータの読み込みとクーポン更新
        loadStampData()
        updateCouponsBasedOnStamps()

        return view
    }

    /**
     * SharedPreferences からスタンプ数を読み込む
     */
    private fun loadStampData() {
        val sharedPreferences = activity?.getSharedPreferences("stamps", Context.MODE_PRIVATE)
        stampCount = sharedPreferences?.getInt("stampCount", 0) ?: 0
    }

    /**
     * スタンプ数に応じたクーポンをリストに追加
     */
    private fun updateCouponsBasedOnStamps() {
        val coupons = mutableListOf<Coupon>()

        if (stampCount >= 3) {
            coupons.add(Coupon("株式会社〇〇", "やきとり", "10%OFF", "2025年12月31日"))
        }
        if (stampCount >= 5) {
            coupons.add(Coupon("株式会社〇〇", "やきとり", "15%OFF", "2025年12月31日"))
        }
        if (stampCount >= 7) {
            coupons.add(Coupon("株式会社〇〇", "やきとり", "20%OFF", "2025年12月31日"))
        }

        // アダプターを更新
        couponAdapter.updateCoupons(coupons)
    }
}
