package com.example.nuka2024_try.ui.qr_scanner

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.MainActivity
import com.example.nuka2024_try.R
import com.example.nuka2024_try.ui.stamps.StampsFragment
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class QRCodeCaptureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        // 縦画面固定
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // QRコードスキャンの設定
        val integrator = IntentIntegrator(this)
        integrator.setCaptureActivity(CaptureActivity::class.java)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("QRコードを読み取ってみよう！")
        integrator.initiateScan()

        // ホームボタンの設定
        val homeButton = findViewById<Button>(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            // QRコードの内容をチェック
            if (result.contents == "yakitori_zen_qr") {
                // yakitori_icon_stamp が読み取られた場合
                val intent = Intent(this, StampsFragment::class.java).apply {
                    putExtra("stampCode", "yakitori")
                }
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
