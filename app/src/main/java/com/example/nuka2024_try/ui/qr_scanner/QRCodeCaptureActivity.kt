package com.example.nuka2024_try.ui.qr_scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONArray

class QRCodeCaptureActivity : AppCompatActivity() {

    // ZXingのスキャン結果を受け取るランチャー
    private val qrCodeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val intentData: Intent? = result.data
            val qrResult: IntentResult? = IntentIntegrator.parseActivityResult(
                result.resultCode,
                intentData
            )
            if (qrResult != null && qrResult.contents != null) {
                val scannedCode = qrResult.contents
                Log.d("QRCodeCaptureActivity", "読み取ったQRコード: $scannedCode")

                // スタンプコードをSharedPreferencesに保存
                saveStampCode(scannedCode)

                // Firestoreへも保存（ユーザーごとに）
                saveStampToFirestore(scannedCode)

                finish()  // スキャナ画面を閉じてStampsFragmentへ戻る想定
            } else {
                Toast.makeText(this, "QRコードが検出されませんでした", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ZXingのスキャナを起動
        IntentIntegrator(this).apply {
            setCaptureActivity(PortraitCaptureActivity::class.java) // 縦画面用Activityがあれば指定
            setOrientationLocked(true)
            setPrompt("QRコードを読み取ってみよう！")
        }.also { integrator ->
            qrCodeLauncher.launch(integrator.createScanIntent())
        }
    }

    /**
     * SharedPreferencesにスタンプコードを保存
     */
    private fun saveStampCode(newCode: String) {
        val sharedPref = getSharedPreferences("Stamps", Context.MODE_PRIVATE)
        val currentList = loadStringArray("stamps").toMutableList()

        if (currentList.contains(newCode)) {
            Toast.makeText(this, "このスタンプは取得済みです: $newCode", Toast.LENGTH_SHORT).show()
        } else {
            currentList.add(newCode)
            Toast.makeText(this, "新しいスタンプを追加しました: $newCode", Toast.LENGTH_SHORT).show()
        }

        // JSON配列として保存
        val jsonArray = JSONArray(currentList)
        sharedPref.edit()
            .putString("stamps", jsonArray.toString())
            .apply()
    }

    /**
     * Firestoreの "stamps/{uid}/codes" にスタンプを保存
     */
    private fun saveStampToFirestore(newCode: String) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(this, "ログインしていません", Toast.LENGTH_SHORT).show()
            return
        }
        val uid = user.uid
        val db = Firebase.firestore

        // ドキュメントID をスタンプコードとして保存
        val docRef = db.collection("stamps")
            .document(uid)
            .collection("codes")
            .document(newCode)

        val stampData = mapOf(
            "id" to newCode,
            "description" to "スキャンしたスタンプ: $newCode",
            "imageUrl" to "circle_app_icon" // 仮の値。実際は画像URLなど
        )

        docRef.set(stampData)
            .addOnSuccessListener {
                Log.d("QRCodeCaptureActivity", "Firestoreにスタンプを保存: $newCode")
            }
            .addOnFailureListener { e ->
                Log.e("QRCodeCaptureActivity", "Firestore保存失敗: $newCode", e)
            }
    }

    /**
     * JSON配列を読み込んでリストへ
     */
    private fun loadStringArray(key: String): List<String> {
        val sharedPref = getSharedPreferences("Stamps", Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString(key, null) ?: return emptyList()
        val jsonArray = JSONArray(jsonString)
        val result = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            result.add(jsonArray.getString(i))
        }
        return result
    }
}
