package com.example.nuka2024_try.ui.stores

import StoresAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.nuka2024_try.R

class StoresFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var recyclerView: RecyclerView
    private val imageList = listOf(
        R.drawable.icon,  // 画像リソースを設定
        R.drawable.nav_icon_stamp,
        R.drawable.circle_app_icon
    )

    // 仮の店舗データ
    private val storeList = listOf(
        Store(R.drawable.icon, "Store 1"),
        Store(R.drawable.nav_icon_stamp, "Store 2"),
        Store(R.drawable.circle_app_icon, "Store 3"),
        Store(R.drawable.icon, "Store 4")
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

        // スライドショーの設定
        viewPager = view.findViewById(R.id.viewPager)
        val adapter = ImageSliderAdapter(imageList)
        viewPager.adapter = adapter

        // 自動スライド機能
        autoSlideImages()

        // RecyclerViewの設定（2列の店舗一覧）
        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(context, 2) // 2列表示に設定
        recyclerView.layoutManager = gridLayoutManager

        // 店舗一覧のアダプターをセット
        val storeAdapter = StoresAdapter(storeList)
        recyclerView.adapter = storeAdapter
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
