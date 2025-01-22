package com.example.nuka2024_try.ui.coupons

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.databinding.FragmentCouponsBinding

class CouponsFragment : Fragment() {

    private var _binding: FragmentCouponsBinding? = null
    private val binding get() = _binding!!

    // クーポンデータのリスト
    private val coupons = mutableListOf(
        Coupon("やきとり 膳", "やきとり", "10%OFF", "2025年○月○日○時○分", false),
        Coupon("たこやき太郎", "たこ焼き", "15%OFF", "2025年○月○日○時○分", false),
        Coupon("蛇之目寿司", "にぎり寿司", "20%OFF", "2025年○月○日○時○分", false)
    )

    // CouponAdapterをクラスメンバーとして定義
    private lateinit var adapter: CouponAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // クーポンの状態を復元
        loadCouponStates()

        // RecyclerViewの設定
        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        // Adapterを初期化
        adapter = CouponAdapter(coupons) { position ->
            // ボタンが押されたときにクーポンの状態を変更
            coupons[position].isUsed = true
            saveCouponState(position) // 使用状態を保存
            adapter.notifyItemChanged(position)
        }
        binding.recyclerViewCoupons.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCoupons.adapter = adapter
    }

    // SharedPreferences にクーポンの状態を保存
    private fun saveCouponState(position: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("coupon_states", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("coupon_$position", true)
        editor.apply()
    }

    // SharedPreferences からクーポンの状態を読み込む
    private fun loadCouponStates() {
        val sharedPreferences = requireContext().getSharedPreferences("coupon_states", Context.MODE_PRIVATE)
        for (i in coupons.indices) {
            coupons[i].isUsed = sharedPreferences.getBoolean("coupon_$i", false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
