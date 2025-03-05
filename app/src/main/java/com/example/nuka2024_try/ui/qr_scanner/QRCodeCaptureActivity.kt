package com.example.nuka2024_try.ui.qr_scanner

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

                // Firestoreにスタンプを保存
                saveStampToFirestore(scannedCode)

                // スキャナ画面を閉じて呼び出し元に戻る
                finish()
            } else {
                Toast.makeText(this, "QRコードが検出されませんでした", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ZXingのスキャナを起動
        IntentIntegrator(this).apply {
            setCaptureActivity(PortraitCaptureActivity::class.java) // 縦画面固定用のカスタムActivityがあれば指定
            setOrientationLocked(true)
            setPrompt("QRコードを読み取ってみよう！")
        }.also { integrator ->
            qrCodeLauncher.launch(integrator.createScanIntent())
        }
    }

    /**
     * Firestoreの "stamps" コレクション配下に
     *  - ユーザーごとのドキュメント (UID)
     *  - その下のサブコレクション "codes"
     *  にスタンプコードを保存する
     */
    private fun saveStampToFirestore(newCode: String) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(this, "ログインしていません", Toast.LENGTH_SHORT).show()
            return
        }
        val uid = user.uid
        val db = Firebase.firestore

        // 例: stamps/{uid}/codes/{スタンプコード} という構造
        val docRef = db.collection("stamps")
            .document(uid)
            .collection("codes")
            .document(newCode)

        // スタンプ情報（任意のフィールドを含められる）
        val stampData = mapOf(
            "id" to newCode,
            "description" to "スキャンしたスタンプ: $newCode",
            "imageUrl" to "circle_app_icon" // 仮の値
        )

        // まず既に同じスタンプコードが登録済みか確認
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // 既に同じコードが登録されている場合
                    Toast.makeText(this, "このスタンプは取得済みです: $newCode", Toast.LENGTH_SHORT).show()
                } else {
                    // 未登録の場合は新規作成
                    docRef.set(stampData)
                        .addOnSuccessListener {
                            Log.d("QRCodeCaptureActivity", "Firestoreにスタンプを保存: $newCode")
                            Toast.makeText(this, "新しいスタンプを追加しました: $newCode", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.e("QRCodeCaptureActivity", "Firestore保存失敗: $newCode", e)
                            Toast.makeText(this, "Firestore保存失敗: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("QRCodeCaptureActivity", "Firestore取得失敗: $newCode", e)
                Toast.makeText(this, "Firestore取得失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
