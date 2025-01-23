package com.example.nuka2024_try.ui.qr_scanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONArray

class QRCodeCaptureActivity : AppCompatActivity() {

    // ActivityResultLauncher を作成（スキャンアプリから結果を受け取る）
    private val qrCodeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("QRCodeCaptureActivity", "Activity result received: ${result.resultCode}")

            val intentData: Intent? = result.data
            val qrResult: IntentResult? = IntentIntegrator.parseActivityResult(
                result.resultCode,
                intentData
            )

            if (qrResult != null && qrResult.contents != null) {
                val scannedCode = qrResult.contents
                Log.d("QRCodeCaptureActivity", "QRコードを読み取ってみよう！: $scannedCode")

                // QRコードの文字列を Int に変換
                val codeInt = try {
                    scannedCode.toInt()
                } catch (e: NumberFormatException) {
                    // 数字でない場合は無効として扱う
                    Toast.makeText(this, "このQRCodeは無効です", Toast.LENGTH_LONG).show()
                    finish()
                    return@registerForActivityResult
                }

                val shiftedIndex = codeInt - 1

                if (shiftedIndex >= 0) {
                    // スタンプの保存
                    saveStamp(shiftedIndex)
                    Toast.makeText(this, "取得したQRコードはこちらです→: $scannedCode", Toast.LENGTH_LONG).show()
                } else {
                    // 0未満なら無効として扱う
                    Toast.makeText(this, "このQRCodeは無効です: $scannedCode", Toast.LENGTH_LONG).show()
                }
                finish()
            } else {
                Toast.makeText(this, "No QR Code detected.", Toast.LENGTH_SHORT).show()
                Log.d("QRCodeCaptureActivity", "QR Code contents were null or scanning was canceled.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 画面レイアウトを表示したい場合は setContentView(R.layout.fragment_qrcode) を使う
        // setContentView(R.layout.fragment_qrcode)

        // ZXing のスキャナを呼び出し、起動する
        IntentIntegrator(this).apply {
            // 先ほど作成・修正した PortraitCaptureActivity を指定
            setCaptureActivity(PortraitCaptureActivity::class.java)
            setOrientationLocked(true)
            setPrompt("QRコードを読み取ってみよう！")
            // 必要に応じてBeep音やバーコード画像の保存設定など
        }.also { integrator ->
            qrCodeLauncher.launch(integrator.createScanIntent())
        }
    }

    /**
     * スタンプをSharedPreferencesに保存する
     */
    private fun saveStamp(index: Int) {
        Log.d("QRCodeCaptureActivity", "Saving stamp: $index")
        val sharedPreferences = getSharedPreferences("Stamps", Context.MODE_PRIVATE)
        val currentData = loadNumberArray("stamps").toMutableSet()
        currentData.add(index)
        val jsonArray = JSONArray(currentData.toList())
        sharedPreferences.edit().putString("stamps", jsonArray.toString()).apply()
        Log.d("QRCodeCaptureActivity", "Stamp saved: $jsonArray")
    }

    private fun loadNumberArray(key: String): List<Int> {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Stamps", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(key, null) ?: return emptyList()
        val jsonArray = JSONArray(jsonString)
        val numbers = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()) {
            numbers.add(jsonArray.getInt(i))
        }
        return numbers
    }
}
