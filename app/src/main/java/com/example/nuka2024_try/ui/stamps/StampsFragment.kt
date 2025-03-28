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
import com.google.firebase.storage.FirebaseStorage

class StampsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_stamps, container, false)

        val stampTextView = view.findViewById<TextView>(R.id.stampCountTextView)
        val stampContainer = view.findViewById<FlexboxLayout>(R.id.stampContainer)

        // FlexboxLayout の配置を左寄せに設定
        stampContainer.justifyContent = JustifyContent.FLEX_START

        val user = Firebase.auth.currentUser ?: return view
        val email = user.email ?: return view

        val db = Firebase.firestore

        // currentStamps/{email} のドキュメントからスタンプID一覧を取得
        db.collection("currentStamps")
            .document(email)
            .get()
            .addOnSuccessListener { docSnap ->
                if (!docSnap.exists()) {
                    // スタンプが無い
                    stampTextView.text = "スタンプ：0"
                    return@addOnSuccessListener
                }

                // 例: { "sample": true, "circle_app_icon": true }
                val dataMap = docSnap.data ?: emptyMap<String, Any>()
                val stampCodes = dataMap.entries
                    .filter { it.value == true }
                    .map { it.key }

                stampTextView.text = "スタンプ：${stampCodes.size}"

                // 既存ビューをクリア
                stampContainer.removeAllViews()

                if (stampCodes.isEmpty()) {
                    val noStampText = TextView(requireContext()).apply {
                        text = "スタンプは所持していません。"
                    }
                    stampContainer.addView(noStampText)
                    return@addOnSuccessListener
                }

                // stampCodes の各コードに対して、Firestore と Storage からデータを取得
                for (code in stampCodes) {
                    fetchStampData(code) { stampInfo ->
                        // stampInfo が null の場合は取得失敗
                        if (stampInfo == null) return@fetchStampData

                        // item_single_stamp.xml をインフレート
                        val stampItemView = inflater.inflate(R.layout.item_single_stamp, stampContainer, false)
                        val stampImage = stampItemView.findViewById<ImageView>(R.id.stampImage)
                        val stampLabel = stampItemView.findViewById<TextView>(R.id.stampLabel)

                        // Firestore からの説明 or 名前を表示
                        stampLabel.text = stampInfo.description

                        // 画像URLが空文字の場合は「画像が取得できません」と表示
                        if (stampInfo.imageUrl.isEmpty()) {
                            stampLabel.text = "${stampInfo.description}\n画像が取得できません"
                        } else {
                            // Glide で画像読み込み
                            Glide.with(requireContext())
                                .load(stampInfo.imageUrl)
                                .into(stampImage)
                        }

                        stampContainer.addView(stampItemView)
                    }
                }
            }
            .addOnFailureListener {
                // currentStamps/{email} 取得失敗時
            }

        return view
    }

    /**
     * CurrentStamps.vue の fetchStampData(id) 相当の処理。
     * 1) Firestore の stamps/{id} からスタンプ情報を取得
     * 2) Storage の stamps/{id}.webp から画像URLを取得
     * まとめて StampInfo オブジェクトとしてコールバックで返す
     */
    private fun fetchStampData(code: String, callback: (StampInfo?) -> Unit) {
        val db = Firebase.firestore
        val stampDocRef = db.collection("stamps").document(code)

        stampDocRef.get().addOnSuccessListener { stampDoc ->
            if (!stampDoc.exists()) {
                // Firestore にスタンプ情報が存在しない場合
                callback(null)
                return@addOnSuccessListener
            }

            // Firestore ドキュメントの情報を取得
            // "description" フィールドがあればそれを使い、無ければ code をそのまま説明にする
            val description = stampDoc.getString("description") ?: code

            // Firebase Storage の stamps/{code}.webp から画像URLを取得
            val storageRef = FirebaseStorage.getInstance()
                .getReference("stamps/$code.webp")

            storageRef.downloadUrl
                .addOnSuccessListener { uri ->
                    // 画像URL取得成功
                    val stampInfo = StampInfo(code, description, uri.toString())
                    callback(stampInfo)
                }
                .addOnFailureListener {
                    // 画像の取得に失敗 → imageUrl は空文字にして返す
                    val stampInfo = StampInfo(code, description, "")
                    callback(stampInfo)
                }
        }.addOnFailureListener {
            // Firestore 取得失敗
            callback(null)
        }
    }
}
