package com.example.nuka2024_try.ui.qr_scanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class QRCodeCaptureActivity : AppCompatActivity() {

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
                // stamps コレクションに存在するか確認し、存在すれば currentStamps に追加
                validateAndSaveStamp(scannedCode)
            } else {
                Toast.makeText(this, "QRコードが検出されませんでした", Toast.LENGTH_SHORT).show()
            }
            finish() // スキャナ画面を閉じる
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ZXingのスキャナを起動
        IntentIntegrator(this).apply {
            setCaptureActivity(PortraitCaptureActivity::class.java) // 縦画面固定用Activity
            setOrientationLocked(true)
            setPrompt("QRコードを読み取ってみよう！")
        }.also { integrator ->
            qrCodeLauncher.launch(integrator.createScanIntent())
        }
    }

    /**
     * stamps/{scannedCode} が存在するか確認し、存在する場合のみ
     * currentStamps/{email} のドキュメントにスタンプを追加する
     */
    private fun validateAndSaveStamp(scannedCode: String) {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Toast.makeText(this, "ログインしていません", Toast.LENGTH_SHORT).show()
            return
        }
        val email = user.email
        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "ユーザーのメールアドレスが取得できません", Toast.LENGTH_SHORT).show()
            return
        }

        val db = Firebase.firestore
        val stampDocRef = db.collection("stamps").document(scannedCode)

        stampDocRef.get()
            .addOnSuccessListener { stampDoc ->
                if (!stampDoc.exists()) {
                    // stamps コレクションに無い → 無効なQRコード
                    Toast.makeText(this, "無効なQRコードです: $scannedCode", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                // stamps/{scannedCode} が存在する → currentStamps に追加
                saveToCurrentStamps(email, scannedCode)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "スタンプ情報の確認に失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * currentStamps/{email} ドキュメントに、stampCode を { stampCode: true } として追加 (merge)
     */
    private fun saveToCurrentStamps(email: String, stampCode: String) {
        val db = Firebase.firestore
        val docRef = db.collection("currentStamps").document(email)
        val data = mapOf(stampCode to true)
        docRef.set(data, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "スタンプを追加しました: $stampCode", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "スタンプ追加失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
