package com.example.nuka2024_try.ui.stamps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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

        // 左揃えに設定
        stampContainer.justifyContent = JustifyContent.FLEX_START

        val db = Firebase.firestore
        val auth = Firebase.auth
        val user = auth.currentUser ?: return view
        val email = user.email ?: return view

        // currentStamps/{email} のドキュメントを取得
        db.collection("currentStamps")
            .document(email)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) {
                    // スタンプがない場合
                    stampTextView.text = "スタンプ：0"
                    return@addOnSuccessListener
                }
                // doc.data は { "circle_app_icon": true, "sample": true, ... } となる
                val dataMap = doc.data ?: emptyMap<String, Any>()
                val stampCodes = dataMap.keys.toList()
                stampTextView.text = "スタンプ：${stampCodes.size}"

                // 既存のビューをクリア
                stampContainer.removeAllViews()

                // 各スタンプコードについて、stamps コレクションから情報を取得して表示
                for (code in stampCodes) {
                    db.collection("stamps")
                        .document(code)
                        .get()
                        .addOnSuccessListener { stampDoc ->
                            val stampItemView = inflater.inflate(R.layout.item_single_stamp, stampContainer, false)
                            val stampImage = stampItemView.findViewById<ImageView>(R.id.stampImage)
                            val stampLabel = stampItemView.findViewById<TextView>(R.id.stampLabel)

                            // stamps ドキュメントの情報を取得
                            val stampId = stampDoc.getString("id") ?: code
                            val imageUrl = stampDoc.getString("imageUrl") ?: ""
                            stampLabel.text = stampId

                            if (imageUrl.isNotEmpty()) {
                                Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.app_icon)
                                    .error(R.drawable.app_icon)
                                    .into(stampImage)
                            } else {
                                stampImage.setImageResource(R.drawable.app_icon)
                            }
                            stampContainer.addView(stampItemView)
                        }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                        }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }

        return view
    }
}
