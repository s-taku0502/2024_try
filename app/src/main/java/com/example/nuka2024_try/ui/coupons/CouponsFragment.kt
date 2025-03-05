package com.example.nuka2024_try.ui.coupons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.databinding.FragmentCouponsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CouponsFragment : Fragment() {

    private var _binding: FragmentCouponsBinding? = null
    private val binding get() = _binding!!

    // クーポンを表示するためのリストとアダプタ
    private val couponList = mutableListOf<Coupon>()
    private lateinit var adapter: CouponAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponsBinding.inflate(inflater, container, false)
        val root = binding.root

        // RecyclerView初期化
        adapter = CouponAdapter(couponList) { coupon ->
            // 「クーポンを使用する」ボタンが押されたときの処理
            useCoupon(coupon)
        }
        binding.recyclerViewCoupons.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCoupons.adapter = adapter

        // ユーザーのスタンプ数を取得し、その数に応じたクーポンを表示
        loadUserStampsCount { userStampsCount ->
            // userStampsCount が 5 なら store_002 が解放され、
            // 7 なら store_003 が解放されるようにしたい場合は、
            // Firestore 側で requiredStamps を 5, 7 に設定しておけばOK。
            loadCouponsFromFirestore(userStampsCount)
        }

        return root
    }

    /**
     * 1) stamps/{uid}/codes のドキュメント数を取得してスタンプ数を判定
     */
    private fun loadUserStampsCount(onResult: (Int) -> Unit) {
        val user = Firebase.auth.currentUser ?: return
        val uid = user.uid
        val db = Firebase.firestore

        db.collection("stamps")
            .document(uid)
            .collection("codes")
            .get()
            .addOnSuccessListener { snapshot ->
                val count = snapshot.size() // スタンプ数
                onResult(count)
            }
            .addOnFailureListener {
                onResult(0) // 失敗時は0とみなす
            }
    }

    /**
     * 2) coupons コレクションから全クーポンを取得し、
     * ユーザーのスタンプ数 (userStampsCount) に応じてフィルタリング。
     * さらに、users/{uid}/coupons/{couponId} の isUsed も読み込んで反映。
     */
    private fun loadCouponsFromFirestore(userStampsCount: Int) {
        val user = Firebase.auth.currentUser ?: return
        val uid = user.uid
        val db = Firebase.firestore

        db.collection("coupons")
            .get()
            .addOnSuccessListener { snapshot ->
                // 取得成功 → ローカルリストをクリア
                couponList.clear()

                // Firestore上の全クーポンを一旦マッピング
                val allCoupons = snapshot.documents.mapNotNull { doc ->
                    val storeName = doc.getString("storeName") ?: ""
                    val title = doc.getString("title") ?: ""
                    val discount = doc.getString("discount") ?: ""
                    val timestamp = doc.getTimestamp("limit")
                    val dateValue = timestamp?.toDate() // Timestamp→Date
                    val requiredStamps = doc.getLong("requiredStamps") ?: 0L

                    Coupon(
                        id = doc.id,
                        storeName = storeName,
                        title = title,
                        discount = discount,
                        limit = dateValue,
                        requiredStamps = requiredStamps
                    )
                }

                // ユーザーのスタンプ数を満たしているクーポンのみ抽出
                val unlockedCoupons = allCoupons.filter { c ->
                    userStampsCount >= c.requiredStamps
                }

                // 3) ユーザーのクーポン使用状況を取得して isUsed を反映
                db.collection("users")
                    .document(uid)
                    .collection("coupons")
                    .get()
                    .addOnSuccessListener { usedSnapshot ->
                        val usedMap = usedSnapshot.documents.associate { doc ->
                            doc.id to (doc.getBoolean("isUsed") ?: false)
                        }

                        // unlockedCoupons に isUsed を上書きして最終リストを作成
                        val finalCoupons = unlockedCoupons.map { coupon ->
                            val used = usedMap[coupon.id] ?: false
                            coupon.copy(isUsed = used)
                        }

                        couponList.addAll(finalCoupons)
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener {
                        // 失敗した場合はすべて未使用として扱う
                        couponList.addAll(unlockedCoupons)
                        adapter.notifyDataSetChanged()
                    }
            }
            .addOnFailureListener {
                // クーポン一覧の取得失敗時の処理 (必要に応じて実装)
            }
    }

    /**
     * 4) クーポンを使用する
     * users/{uid}/coupons/{couponId} に isUsed = true を書き込み
     */
    private fun useCoupon(coupon: Coupon) {
        val user = Firebase.auth.currentUser ?: return
        val uid = user.uid
        val db = Firebase.firestore

        db.collection("users")
            .document(uid)
            .collection("coupons")
            .document(coupon.id)
            .set(mapOf("isUsed" to true))
            .addOnSuccessListener {
                // ローカルリストを更新して再描画
                coupon.isUsed = true
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // エラー処理
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
