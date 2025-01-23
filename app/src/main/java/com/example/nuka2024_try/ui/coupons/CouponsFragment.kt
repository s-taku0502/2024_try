package com.example.nuka2024_try.ui.coupons

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.databinding.FragmentCouponsBinding
import org.json.JSONArray

class CouponsFragment : Fragment() {

    private var _binding: FragmentCouponsBinding? = null
    private val binding get() = _binding!!

    // ★本来のクーポンデータは、ベースデータとして持つのみ
    private val allCoupons = listOf(
        Coupon("やきとり 膳", "やきとり", "10%OFF", "2025年○月○日○時○分", false),
        Coupon("たこやき太郎", "たこ焼き", "15%OFF", "2025年○月○日○時○分", false),
        Coupon("蛇之目寿司", "にぎり寿司", "20%OFF", "2025年○月○日○時○分", false)
    )

    // 表示用にフィルタリングされたクーポンリスト
    private val displayCoupons = mutableListOf<Coupon>()

    // CouponAdapterをクラスメンバーとして定義
    private lateinit var adapter: CouponAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 1) スタンプ数を取得
        val stampCount = getStampCountFromSharedPrefs()

        // 2) スタンプ数に応じてクーポンをフィルタリングして表示用リストに追加
        //  3枚以上: やきとり(10%OFF)
        //  5枚以上: たこ焼き(15%OFF)
        //  7枚以上: にぎり寿司(20%OFF)
        displayCoupons.clear()
        if (stampCount >= 3) {
            // やきとりを追加
            displayCoupons.add(allCoupons[0])
        }
        if (stampCount >= 5) {
            // たこ焼きを追加
            displayCoupons.add(allCoupons[1])
        }
        if (stampCount >= 7) {
            // にぎり寿司を追加
            displayCoupons.add(allCoupons[2])
        }

        // 3) クーポンの「使用済み状態」を復元 (SharedPreferences)
        loadCouponStates()

        // 4) RecyclerViewの設定
        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        // Adapterを初期化
        adapter = CouponAdapter(displayCoupons) { position ->
            // ボタンが押されたときにクーポンの状態を変更
            displayCoupons[position].isUsed = true
            saveCouponState(position) // 使用状態を保存
            adapter.notifyItemChanged(position)
        }
        binding.recyclerViewCoupons.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCoupons.adapter = adapter
    }

    // SharedPreferences にクーポンの状態を保存
    // ここでは「displayCouponsのi番目」を保存する場合、同じ順番で管理できるように注意が必要です
    // 今回は一番シンプルに「表示されている順番」で保存する実装例を示しています。
    private fun saveCouponState(position: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("coupon_states", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // クーポンのタイトルなどユニークな情報でキーを作ると衝突しにくい
        val uniqueKey = "coupon_${displayCoupons[position].title}"

        editor.putBoolean(uniqueKey, true)
        editor.apply()
    }

    // SharedPreferences からクーポンの状態を読み込む
    private fun loadCouponStates() {
        val sharedPreferences = requireContext().getSharedPreferences("coupon_states", Context.MODE_PRIVATE)

        // 表示対象になったクーポンのみをループして読み込む
        displayCoupons.forEach { coupon ->
            val uniqueKey = "coupon_${coupon.title}"
            coupon.isUsed = sharedPreferences.getBoolean(uniqueKey, false)
        }
    }

    /**
     * 現在のスタンプ数を取得するメソッド
     * QRCodeCaptureActivity で保存している "Stamps" という SharedPreferences から
     * "stamps" というキーで JSON Array を取得し、その要素数をスタンプ数とみなす。
     */
    private fun getStampCountFromSharedPrefs(): Int {
        val sharedPreferences = requireContext().getSharedPreferences("Stamps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("stamps", null) ?: return 0
        val jsonArray = JSONArray(jsonString)
        return jsonArray.length() // 配列の長さをスタンプ数とする
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
