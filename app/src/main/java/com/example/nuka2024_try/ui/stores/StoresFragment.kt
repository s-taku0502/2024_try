// StoresFragment.kt
package com.example.nuka2024_try.ui.stores

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.nuka2024_try.R
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StoresFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var recyclerView: RecyclerView
    private val imageList = listOf(
        R.drawable.icon,  // スライドショー用の画像リソース
        R.drawable.nav_icon_stamp,
        R.drawable.circle_app_icon
    )

    // 2列に表示する画像リスト
    private val storeImages = listOf(
        Store(R.drawable.icon, "Store 1"),
        Store(R.drawable.nav_icon_stamp, "Store 2"),
        Store(R.drawable.circle_app_icon, "Store 3"),
        Store(R.drawable.coupons, "Store 4")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // スライドショー設定
        viewPager = view.findViewById(R.id.viewPager)
        val sliderAdapter = ImageSliderAdapter(imageList)
        viewPager.adapter = sliderAdapter

        // 2列表示のリスト設定
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // 2列に設定
        recyclerView.adapter = StoresAdapter(storeImages) { store ->
            // クリック時の動作（必要に応じて実装）
        }

        // 自動スライドの設定
        autoSlideImages()
    }

    private fun autoSlideImages() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val nextItem = if (currentItem == imageList.size - 1) 0 else currentItem + 1
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000) // 3秒ごとにスライド
            }
        }
        handler.postDelayed(runnable, 3000)
    }
}
