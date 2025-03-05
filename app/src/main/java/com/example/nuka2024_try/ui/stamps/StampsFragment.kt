package com.example.nuka2024_try.ui.stamps

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

class StampsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_stamps, container, false)

        val stampTextView = view.findViewById<TextView>(R.id.stampCountTextView)
        val stampContainer = view.findViewById<FlexboxLayout>(R.id.stampContainer)

        // FlexboxLayoutの子ビューを左揃えにする
        stampContainer.justifyContent = JustifyContent.FLEX_START

        val db = Firebase.firestore
        val auth = Firebase.auth
        val userId = auth.currentUser?.uid ?: return view // ログインしていない場合は何も表示しない

        // ここを "users/{userId}/stamps" ではなく "stamps/{userId}/codes" に変更
        db.collection("stamps")
            .document(userId)
            .collection("codes")
            .get()
            .addOnSuccessListener { result ->
                // 取得したスタンプコードをリスト化
                val codes = result.documents.map { doc ->
                    doc.id  // または doc.getString("id") ?: doc.id
                }

                // スタンプ数を表示
                stampTextView.text = "スタンプ：${codes.size}"

                // 既存の子ビューをクリアしてから追加
                stampContainer.removeAllViews()

                // 各スタンプコードを画面に表示
                for (code in codes) {
                    val stampItemView = inflater.inflate(R.layout.item_single_stamp, stampContainer, false)
                    val stampImage = stampItemView.findViewById<ImageView>(R.id.stampImage)
                    val stampLabel = stampItemView.findViewById<TextView>(R.id.stampLabel)

                    // Firestoreに "imageUrl" フィールドがあれば取得して
                    // Glide などで表示可能（ここでは簡単のため固定アイコン）
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
}
