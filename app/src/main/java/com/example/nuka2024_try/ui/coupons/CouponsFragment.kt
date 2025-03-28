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
import kotlin.random.Random
import java.util.Date

class CouponsFragment : Fragment() {

    private var _binding: FragmentCouponsBinding? = null
    private val binding get() = _binding!!

    // ユーザーが所持しているクーポンのみを表示
    private val couponList = mutableListOf<Coupon>()
    private lateinit var adapter: CouponAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponsBinding.inflate(inflater, container, false)
        val root = binding.root

        // RecyclerView 初期化
        adapter = CouponAdapter(couponList) { coupon ->
            // 「クーポンを使用する」ボタンが押されたときの処理
            useCoupon(coupon)
        }
        binding.recyclerViewCoupons.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCoupons.adapter = adapter

        // 1) ユーザーのスタンプ数を取得 → 2) クーポン配布 → 3) ユーザーが所持しているクーポンを表示
        loadUserStampsCount { userStampsCount ->
            distributeCouponsIfEligible(userStampsCount) {
                loadUserCoupons()
            }
        }

        return root
    }

    /**
     * 1) ユーザーのスタンプ数を取得
     * currentStamps/{email} に { stampCode: true } 形式で保持されている想定
     */
    private fun loadUserStampsCount(onResult: (Int) -> Unit) {
        val user = Firebase.auth.currentUser ?: return onResult(0)
        val email = user.email ?: return onResult(0)
        val db = Firebase.firestore

        db.collection("currentStamps")
            .document(email)
            .get()
            .addOnSuccessListener { docSnap ->
                if (!docSnap.exists()) {
                    onResult(0)
                } else {
                    val dataMap = docSnap.data ?: emptyMap<String, Any>()
                    // 値が true のキーをスタンプとみなす
                    val stampCodes = dataMap.entries.filter { it.value == true }.map { it.key }
                    onResult(stampCodes.size)
                }
            }
            .addOnFailureListener {
                onResult(0)
            }
    }

    /**
     * 2) スタンプ数が 3,5,7,10,15,20, ... に達するたびに、
     * 期限内のクーポンをランダムで1つ配布 (同じクーポンIDの重複配布は防がない)
     */
    private fun distributeCouponsIfEligible(userStampsCount: Int, onComplete: () -> Unit) {
        val user = Firebase.auth.currentUser ?: return onComplete()
        val uid = user.uid  // 現在ログイン中のユーザーのUIDを使用
        val db = Firebase.firestore

        val thresholds = generateThresholds(userStampsCount)
        if (thresholds.isEmpty()) {
            onComplete()
            return
        }

        distributeForNextThreshold(db, uid, thresholds, 0, onComplete)
    }

    private fun generateThresholds(stamps: Int): List<Int> {
        val base = listOf(3, 5, 7, 10)
        val result = mutableListOf<Int>()
        for (b in base) {
            if (b <= stamps) result.add(b)
        }
        var t = 15
        while (t <= stamps) {
            result.add(t)
            t += 5
        }
        return result
    }

    private fun distributeForNextThreshold(
        db: com.google.firebase.firestore.FirebaseFirestore,
        uid: String,
        thresholds: List<Int>,
        index: Int,
        finalCallback: () -> Unit
    ) {
        if (index >= thresholds.size) {
            finalCallback()
            return
        }
        val currentThreshold = thresholds[index]

        // チェック: 現在のUIDのユーザーに対して、既にこの閾値のクーポンが配布済みか
        db.collection("users")
            .document(uid)
            .collection("coupons")
            .whereEqualTo("threshold", currentThreshold)
            .get()
            .addOnSuccessListener { snap ->
                if (!snap.isEmpty) {
                    // 既にこの閾値のクーポンが配布済み → 次の閾値へ
                    distributeForNextThreshold(db, uid, thresholds, index + 1, finalCallback)
                } else {
                    // coupons コレクションから期限内のクーポンを全取得 → ランダムに1件選択
                    db.collection("coupons")
                        .get()
                        .addOnSuccessListener { couponsSnap ->
                            val now = Date()
                            val validDocs = couponsSnap.documents.filter { doc ->
                                val limitTimestamp = doc.getTimestamp("limit")
                                val limitDate = limitTimestamp?.toDate()
                                // limit が null の場合は期限なしとみなす
                                limitDate == null || limitDate.after(now)
                            }
                            if (validDocs.isEmpty()) {
                                distributeForNextThreshold(db, uid, thresholds, index + 1, finalCallback)
                                return@addOnSuccessListener
                            }
                            // ランダムに1件選択
                            val randomDoc = validDocs[Random.nextInt(validDocs.size)]
                            val couponId = randomDoc.id
                            val discount = randomDoc.getString("discount") ?: ""
                            val storeName = randomDoc.getString("storeName") ?: ""
                            val title = randomDoc.getString("title") ?: ""
                            val limitTimestamp = randomDoc.getTimestamp("limit")

                            val couponData = mapOf(
                                "isUsed" to false,
                                "discount" to discount,
                                "storeName" to storeName,
                                "title" to title,
                                "limit" to limitTimestamp,
                                "threshold" to currentThreshold
                            )
                            // 同じクーポンIDでも threshold が違えば別ドキュメントにするため、ID に threshold を付与
                            val docId = "${couponId}_${currentThreshold}"
                            db.collection("users")
                                .document(uid)
                                .collection("coupons")
                                .document(docId)
                                .set(couponData)
                                .addOnSuccessListener {
                                    distributeForNextThreshold(db, uid, thresholds, index + 1, finalCallback)
                                }
                                .addOnFailureListener {
                                    distributeForNextThreshold(db, uid, thresholds, index + 1, finalCallback)
                                }
                        }
                        .addOnFailureListener {
                            distributeForNextThreshold(db, uid, thresholds, index + 1, finalCallback)
                        }
                }
            }
            .addOnFailureListener {
                distributeForNextThreshold(db, uid, thresholds, index + 1, finalCallback)
            }
    }

    /**
     * 3) ユーザーが所持しているクーポン (users/{uid}/coupons) を読み込み、期限切れ・使用済みを反映して表示
     */
    private fun loadUserCoupons() {
        val user = Firebase.auth.currentUser ?: return
        val uid = user.uid  // 現在ログイン中のユーザーのUIDを使用
        val db = Firebase.firestore

        db.collection("users")
            .document(uid)
            .collection("coupons")
            .get()
            .addOnSuccessListener { snap ->
                couponList.clear()
                val now = Date()

                val userCoupons = snap.documents.map { doc ->
                    val couponId = doc.id
                    val discount = doc.getString("discount") ?: ""
                    val storeName = doc.getString("storeName") ?: ""
                    val title = doc.getString("title") ?: ""
                    val limitTimestamp = doc.getTimestamp("limit")
                    val limitDate = limitTimestamp?.toDate()
                    val isUsed = doc.getBoolean("isUsed") ?: false

                    // 期限切れチェック
                    val isExpired = (limitDate != null && limitDate.before(now))
                    if (isExpired) {
                        Coupon(
                            id = couponId,
                            storeName = storeName,
                            title = title,
                            discount = discount,
                            limit = limitDate,
                            isUsed = true
                        )
                    } else {
                        Coupon(
                            id = couponId,
                            storeName = storeName,
                            title = title,
                            discount = discount,
                            limit = limitDate,
                            isUsed = isUsed
                        )
                    }
                }

                couponList.addAll(userCoupons)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // 取得失敗時の処理
            }
    }

    /**
     * 4) 「クーポンを使用する」ボタンが押されたら、users/{uid}/coupons/{docId} に isUsed=true を書き込み
     */
    private fun useCoupon(coupon: Coupon) {
        val user = Firebase.auth.currentUser ?: return
        val uid = user.uid  // 現在のユーザーのUIDを使用
        val db = Firebase.firestore

        db.collection("users")
            .document(uid)
            .collection("coupons")
            .document(coupon.id)
            .update("isUsed", true)
            .addOnSuccessListener {
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
