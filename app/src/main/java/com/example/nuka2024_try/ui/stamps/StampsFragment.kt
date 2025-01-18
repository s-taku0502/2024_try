package com.example.nuka2024_try.ui.stamps

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nuka2024_try.R
import com.google.android.flexbox.FlexboxLayout

class StampsFragment : Fragment() {

    private var stampCount = 0
    private lateinit var stampTextView: TextView
    private lateinit var stampContainer: FlexboxLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // スタンプ数の読み込み
        loadStampData()

        // QRコード結果を取得
        val qrCodeResult = arguments?.getString("stampCode")
        if (qrCodeResult != null) {
            processQRCode(qrCodeResult)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stamps, container, false)
        stampTextView = view.findViewById(R.id.stampCountTextView)
        stampContainer = view.findViewById(R.id.stampContainer)
        updateStampUI()
        return view
    }

    private fun processQRCode(qrCodeResult: String) {
        when (qrCodeResult) {
            "yakitori_zen_qr" -> addStampWithIcon(R.drawable.yakitori_icon_stamp)
            else -> stampTextView.text = "無効なQRコードです"
        }
    }

    private fun addStampWithIcon(iconResId: Int) {
        val imageView = ImageView(context)
        imageView.setImageResource(iconResId)
        imageView.layoutParams = LinearLayout.LayoutParams(100, 100)
        stampContainer.addView(imageView)
        stampCount++
        saveStampData()
        updateStampUI()
        checkForCoupons()
    }

    private fun saveStampData() {
        val sharedPreferences = activity?.getSharedPreferences("stamps", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putInt("stampCount", stampCount)
        editor?.apply()
    }

    private fun loadStampData() {
        val sharedPreferences = activity?.getSharedPreferences("stamps", Context.MODE_PRIVATE)
        stampCount = sharedPreferences?.getInt("stampCount", 0) ?: 0
    }

    private fun updateStampUI() {
        stampTextView.text = "スタンプ数: $stampCount"
    }

    private fun checkForCoupons() {
        val sharedPreferences = activity?.getSharedPreferences("coupons", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        when (stampCount) {
            3 -> editor?.putBoolean("coupon_10", true)
            5 -> editor?.putBoolean("coupon_15", true)
            7 -> editor?.putBoolean("coupon_20", true)
        }
        editor?.apply()
    }
}
