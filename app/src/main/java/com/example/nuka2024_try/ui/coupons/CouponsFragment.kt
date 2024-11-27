package com.example.nuka2024_try.ui.coupons

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nuka2024_try.databinding.FragmentCouponsBinding

class CouponsFragment : Fragment() {

    private var _binding: FragmentCouponsBinding? = null

    // このプロパティは onCreateView と onDestroyView の間でのみ有効です
    private val binding get() = _binding!!

    // 店舗データを直接リストで管理
    private val storeList = listOf(
        Store("1", "商店A", "地域の特産品を取り扱っています"),
        Store("2", "商店B", "新鮮な野菜と果物を提供"),
        Store("3", "商店C", "手作りの雑貨が人気")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 初期UIの設定
        setupIntroText()
        displayStoreList()

        return root
    }

    private fun setupIntroText() {
        val textView: TextView = binding.textCoupons

        val spannableText =
            SpannableString("額地区スタンプらり～\nスタンプらり～を活用して、商店街をまわろう！")

        // "スタンプらり～" のサイズと太字設定
        spannableText.setSpan(StyleSpan(Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableText.setSpan(AbsoluteSizeSpan(20, true), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // "商店街をまわろう！" のサイズ設定
        spannableText.setSpan(
            AbsoluteSizeSpan(18, true),
            11,
            spannableText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.setTextColor(Color.BLACK)
        textView.gravity = Gravity.CENTER // テキストを中央揃え
        textView.text = spannableText
    }

    private fun displayStoreList() {
        // 店舗リストを表示する処理を記述
        storeList.forEach { store ->
            println("店舗名: ${store.name}, 説明: ${store.description}, 業種： ${store.description}")
            // 必要に応じてUIに店舗情報を追加
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 店舗情報を管理するデータクラス
    data class Store(val id: String, val name: String, val description: String)
}
