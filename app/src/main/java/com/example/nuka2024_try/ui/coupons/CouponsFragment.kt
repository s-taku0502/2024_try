package com.example.nuka2024_try.ui.coupons

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coupons, container, false)
        recyclerView = view.findViewById(R.id.couponsRecyclerView)

        val coupons = loadCoupons()
        couponAdapter = CouponAdapter(coupons)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = couponAdapter

        return view
    }

    private fun loadCoupons(): List<Coupon> {
        return listOf(
            Coupon("株式会社〇〇", "やきとり", "10%OFF", "2025年〇月〇日〇時〇分"),
            Coupon("株式会社〇〇", "やきとり", "10%OFF", "2025年〇月〇日〇時〇分"),
            Coupon("株式会社〇〇", "やきとり", "10%OFF", "2025年〇月〇日〇時〇分")
        )
    }
}
