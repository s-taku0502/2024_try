package com.example.nuka2024_try.ui.qr_scanner

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.R
import com.example.nuka2024_try.ui.stamps.StampsFragment
import com.google.zxing.integration.android.IntentIntegrator

class QR_Scanner_Main : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        // 縦画面固定
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // QRスキャンボタン
        findViewById<Button>(R.id.qr_button).setOnClickListener {
            IntentIntegrator(this).apply {
                captureActivity = QRCodeCaptureActivity::class.java
            }.initiateScan()
        }

        // ホームボタン
        findViewById<Button>(R.id.homeButton).setOnClickListener {
            finish() // ホーム画面に戻る
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            if (result.contents == "yakitori_zen_qr") {
                val intent = Intent(this, StampsFragment::class.java).apply {
                    putExtra("stampCode", "yakitori")
                }
                startActivity(intent)
            }
        }
    }
}
