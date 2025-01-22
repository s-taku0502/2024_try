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
                "不動産を通じて、人と地域をつなぐ",
                "石川県金沢市大額2丁目65番地",
                URL("https://aurie.jp/"),
                R.drawable.aurie
            ),
            Store(
                "株式会社アスティ",
                "アスティの詳細情報",
                "老人福祉・介護事業",
                "介護事業の将来を守る温かい心のケア",
                "石川県金沢市四十万４丁目１２７－２",
                URL("https://asty-k.jp/"),
                R.drawable.asutei
            ),
            Store(
                "石崎産業株式会社",
                "石崎産業の詳細情報",
                "包装機械製造業",
                "環境配慮と地域貢献を重視した包装資材企業",
                "石川県金沢市大額3丁目194番地",
                URL("http://www.ishizaki-sangyo.com/"),
                R.drawable.ishizakisangyou
            ),
            Store(
                "やきとり 膳",
                "やきとり 膳の詳細情報",
                "飲食業",
                "手づくり焼き鳥で常連を魅了する居酒屋",
                "石川県金沢市窪4-403　本陣ロイヤルハイム1F",
                URL("https://nukashinkoukai.com/shop/417/"),
                R.drawable.yakitori_zen
            ),
            Store(
                "たこやき太郎",
                "たこやき太郎の詳細情報",
                "飲食業",
                "地域の人のお腹を満たす、絶品たこ焼き!",
                "石川県金沢市額新保1-445",
                URL("https://nukashinkoukai.com/shop/420/"),
                R.drawable.takoyaki_tarou
            ),
            Store(
                "蛇之目寿司",
                "蛇之目寿司の詳細情報",
                "飲食業",
                "40年愛される、昔ながらのお寿司屋さん",
                "石川県金沢市大額2-55",
                URL("http://www.kanazawa-jyanome.com/"),
                R.drawable.janomezusi
            )
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
