package com.example.nuka2024_try.ui.coupons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nuka2024_try.databinding.FragmentCouponsBinding

class CouponsFragment : Fragment() {
    private lateinit var binding: FragmentCouponsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCouponsBinding.inflate(inflater, container, false)
        displayCoupons()
        return binding.root
    }

    private fun displayCoupons() {
        val coupons = CouponManager.getCoupons()
        binding.couponsList.text = coupons.joinToString("\n")
    }
}
