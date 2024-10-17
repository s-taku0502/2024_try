// StoresFragment.kt
package com.example.nuka2024_try.ui.stores

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.nuka2024_try.R
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.GlideUrl


class StoresFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private val imageList = listOf(
        R.drawable.icon,  // 画像リソースを設定
        R.drawable.nav_icon_stamp,
        R.drawable.circle_app_icon
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_stores.xml を膨らませる
        return inflater.inflate(R.layout.fragment_stores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        val adapter = ImageSliderAdapter(imageList)
        viewPager.adapter = adapter

        // 自動スライド機能
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

