package com.example.nuka2024_try.ui.stores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.R
import com.example.nuka2024_try.databinding.FragmentStoresBinding
import java.net.URL

class StoreFragment : Fragment() {

    private var _binding: FragmentStoresBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoresBinding.inflate(inflater, container, false)
        val root = binding.root

        // サンプルデータ
        val storeList = listOf(
            Store(
                "株式会社アウリエ",
                "アウリエの詳細情報",
                "不動産",
                "石川県",
                URL("https://sites.google.com/view/daimon-dx/"),
                R.drawable.aurie
            ),
            Store(
                "株式会社アスティ",
                "アスティの詳細情報",
                "業種",
                "石川県",
                URL("https://sites.google.com/view/daimon-dx/"),
                R.drawable.asutei
            ),
            Store(
                "石崎産業株式会社",
                "石崎産業の詳細情報",
                "業種",
                "石川県",
                URL("https://sites.google.com/view/daimon-dx/"),
                R.drawable.ishizakisangyou
            ),
        )

        // RecyclerView のセットアップ
        val recyclerView = binding.storeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // 1列表示
        recyclerView.adapter = StoreAdapter(storeList) // アダプターにデータを渡す

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
