package com.example.nuka2024_try.ui.stores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.databinding.FragmentStoresBinding
import com.google.firebase.firestore.FirebaseFirestore

class StoreFragment : Fragment() {

    private var _binding: FragmentStoresBinding? = null
    private val binding get() = _binding!!

    // Firestore インスタンスを取得
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoresBinding.inflate(inflater, container, false)
        val root = binding.root

        // RecyclerView のセットアップ
        val recyclerView = binding.storeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ★ Firestore の "stores" コレクションからデータを取得
        firestore.collection("stores")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val storeList = mutableListOf<Store>()
                for (document in querySnapshot) {
                    // Firestore 上のドキュメントID (例: "AURIE")
                    val documentId = document.id

                    // Firestore の各フィールドを取得（nullなら空文字にする）
                    val name = document.getString("name") ?: ""
                    val description = document.getString("description") ?: ""
                    val industry = document.getString("industry") ?: ""
                    val companyFeatures = document.getString("company_features") ?: ""
                    val address = document.getString("address") ?: ""
                    val websiteUrl = document.getString("website_url") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""

                    // Store データクラスのインスタンスを作成
                    val store = Store(
                        documentId = documentId,
                        name = name,
                        description = description,
                        industry = industry,
                        company_features = companyFeatures,
                        address = address,
                        website_url = websiteUrl,
                        imageUrl = imageUrl,
                        isExpanded = false
                    )
                    storeList.add(store)
                }
                // 取得したリストをアダプターにセット
                recyclerView.adapter = StoreAdapter(storeList)
            }
            .addOnFailureListener { e ->
                // 取得失敗時の処理（エラー表示など）
                e.printStackTrace()
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
