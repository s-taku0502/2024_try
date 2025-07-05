package com.example.nuka2024_try.ui.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.example.nuka2024_try.R
import com.example.nuka2024_try.databinding.FragmentHomeBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBannerText()
        observeNewsCollection()
    }

    private fun setupBannerText() {
        if (_binding == null) return
        binding.textBannerHome.text = "ホーム"
        binding.textLine1.text = "額地区スタンプらり～"
        binding.textLine2.text = "スタンプらり～を活用して、商店街を周ろう！"
    }


    /**
     * Firestore の "news" コレクションをリアルタイム監視し、Home画面のお知らせに反映する
     * ・orderBy("endDate", Query.Direction.DESCENDING) により、endDate の新しい順（降順）で取得
     * ・各お知らせは RelativeLayout を用いて作成し、本文は太文字で、更新日は細く薄い文字で本文の下／右下に配置
     */
    private fun observeNewsCollection() {
        db.collection("news")
            .orderBy("endDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("HomeFragment", "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (_binding == null) return@addSnapshotListener

                val newsContainer = binding.newsContainer
                newsContainer.removeAllViews() // 一旦クリア

                if (snapshot != null) {
                    // Firestore のクエリで endDate の降順（新しい順）に取得されるので、
                    // その順番でループし、順次レイアウトに追加する。
                    for (document in snapshot.documents) {
                        val endDate = document.getString("endDate") ?: ""
                        val content = document.getString("content") ?: ""

                        // RelativeLayout を使って各お知らせのアイテムレイアウトを生成
                        val itemLayout = RelativeLayout(requireContext()).apply {
                            setPadding(resources.getDimensionPixelSize(R.dimen.default_padding))
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }

                        // お知らせ内容の TextView (太文字) — 左上に配置
                        val contentTextView = TextView(requireContext()).apply {
                            id = View.generateViewId()
                            text = content
                            textSize = 16f
                            setTypeface(typeface, Typeface.BOLD)
                        }
                        val contentParams = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            addRule(RelativeLayout.ALIGN_PARENT_TOP)
                            addRule(RelativeLayout.ALIGN_PARENT_START)
                            addRule(RelativeLayout.ALIGN_PARENT_END)
                        }
                        itemLayout.addView(contentTextView, contentParams)

                        // 更新日の日付 TextView (細く薄い文字) — 内容の下に、右端に配置
                        val dateTextView = TextView(requireContext()).apply {
                            id = View.generateViewId()
                            text = endDate
                            textSize = 14f
                            setTypeface(null, Typeface.NORMAL)
                            setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_gray))
                        }
                        val dateParams = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            addRule(RelativeLayout.ALIGN_PARENT_END)
                            addRule(RelativeLayout.BELOW, contentTextView.id)
                            topMargin = resources.getDimensionPixelSize(R.dimen.default_padding) / 2
                        }
                        itemLayout.addView(dateTextView, dateParams)

                        // アイテムレイアウトをお知らせコンテナに追加
                        newsContainer.addView(itemLayout)

                        // 区切り線（divider）を追加
                        val dividerView = View(requireContext()).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                resources.getDimensionPixelSize(R.dimen.divider_height)
                            )
                            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                        }
                        newsContainer.addView(dividerView)
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
