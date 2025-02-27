package com.example.nuka2024_try.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nuka2024_try.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // --- 上部バナー部分のテキスト設定 ---
        val bannerText = "額地区スタンプらり～\nスタンプらり～を活用して、商店街を周ろう！"
        val spannable = SpannableString(bannerText)
        // 「スタンプらり～」部分を20spに設定 (indices 0～10)
        spannable.setSpan(AbsoluteSizeSpan(20, true), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        // 「商店街を周ろう！」部分を18spに設定 (indices 24～32)
        spannable.setSpan(AbsoluteSizeSpan(18, true), 24, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.textHome.text = spannable
        binding.textHome.setTextColor(Color.BLACK)

        // --- Firestoreから「news」コレクションのデータを取得 ---
        val newsContainer = binding.newsContainer
        val db = Firebase.firestore

        db.collection("news")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Firebase上のフィールド名に合わせて取得
                    val endData = document.getString("endDate") ?: ""
                    val content = document.getString("content") ?: ""

                    // お知らせ1件のレイアウト
                    val itemLayout = LinearLayout(requireContext()).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(16, 16, 16, 16)
                    }

                    // 1. 日付のTextView（太字にするとより分かりやすい）
                    val dateTextView = TextView(requireContext()).apply {
                        text = endData
                        textSize = 16f
                        setTypeface(typeface, Typeface.BOLD) // 太字
                        setTextColor(Color.BLACK)
                    }

                    // 2. お知らせ内容のTextView
                    val contentTextView = TextView(requireContext()).apply {
                        text = content
                        textSize = 14f
                        setTextColor(Color.DKGRAY)
                    }

                    // レイアウトに追加
                    itemLayout.addView(dateTextView)
                    itemLayout.addView(contentTextView)

                    // 3. 区切り線（divider）を追加して見やすく
                    val dividerView = View(requireContext()).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            2 // 高さ2px
                        )
                        setBackgroundColor(Color.LTGRAY)
                    }

                    // お知らせレイアウトを newsContainer に追加
                    newsContainer.addView(itemLayout)
                    newsContainer.addView(dividerView)
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
