package com.example.nuka2024_try.ui.stamps

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nuka2024_try.R
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONArray

class StampsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_stamps, container, false)

        val stampTextView = view.findViewById<TextView>(R.id.stampCountTextView)
        val stampContainer = view.findViewById<FlexboxLayout>(R.id.stampContainer)

        // FlexboxLayoutの子ビューを左揃えに設定
        stampContainer.justifyContent = JustifyContent.FLEX_START

        // SharedPreferencesからローカルのスタンプコードを読み込み
        val localCodes = loadStringArray("stamps")

        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            // 未ログインの場合はローカルのみ表示
            val sortedLocal = localCodes.mapNotNull { it.toIntOrNull() }
                .sorted()
                .map { it.toString() }
            stampTextView.text = "スタンプ：${sortedLocal.size}"
            for (code in sortedLocal) {
                val stampItemView = inflater.inflate(R.layout.item_single_stamp, stampContainer, false)
                val stampImage = stampItemView.findViewById<ImageView>(R.id.stampImage)
                val stampLabel = stampItemView.findViewById<TextView>(R.id.stampLabel)
                stampImage.setImageResource(R.drawable.app_icon)
                stampLabel.text = code
                stampContainer.addView(stampItemView)
            }
            return view
        }

        // ログイン中ユーザーの uid を利用して Firestore からスタンプを取得
        val uid = currentUser.uid
        val db = Firebase.firestore
        db.collection("stamps")
            .document(uid)
            .collection("codes")
            .get()
            .addOnSuccessListener { result ->
                val firestoreCodes = mutableListOf<String>()
                for (doc in result) {
                    val code = doc.getString("id") ?: ""
                    if (code.isNotEmpty()) {
                        firestoreCodes.add(code)
                    }
                }

                // ローカルとFirestoreのコードをマージ（重複除外）
                val allCodes = (localCodes + firestoreCodes).distinct()

                // 数値としてソート
                val sortedCodes = allCodes
                    .mapNotNull { it.toIntOrNull() }
                    .sorted()
                    .map { it.toString() }

                // スタンプ数を表示
                stampTextView.text = "スタンプ：${sortedCodes.size}"

                // FlexboxLayoutにスタンプアイテムを追加
                stampContainer.removeAllViews()
                for (code in sortedCodes) {
                    val stampItemView = inflater.inflate(R.layout.item_single_stamp, stampContainer, false)
                    val stampImage = stampItemView.findViewById<ImageView>(R.id.stampImage)
                    val stampLabel = stampItemView.findViewById<TextView>(R.id.stampLabel)
                    stampImage.setImageResource(R.drawable.app_icon)
                    stampLabel.text = code
                    stampContainer.addView(stampItemView)
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }

        return view
    }

    /**
     * SharedPreferences ("Stamps") の "stamps"キーに保存された
     * JSON配列をStringのリストとして読み込む
     */
    private fun loadStringArray(key: String): List<String> {
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences("Stamps", Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString(key, null) ?: return emptyList()
        val jsonArray = JSONArray(jsonString)
        val result = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            result.add(jsonArray.getString(i))
        }
        return result
    }
}
