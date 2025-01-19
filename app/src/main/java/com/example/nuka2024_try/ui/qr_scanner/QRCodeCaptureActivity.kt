package com.example.nuka2024_try.ui.qr_scanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.MainActivity
import com.example.nuka2024_try.databinding.ActivityQrscannerBinding

class QRCodeCaptureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrscannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ホーム画面に戻るボタンのクリックリスナー
        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // QRコードスキャンの初期化 (簡易例)
        initializeQRScanner()
    }

    private fun initializeQRScanner() {
        // QRコードスキャンのロジック (仮のデモ用)
        // 実際のライブラリ(ZXing等)を使用して実装する
        val scannedQRCode = "yakitori_zen_qr" // 仮定: スキャンしたQRコードの値
        if (scannedQRCode == "yakitori_zen_qr") {
            StampsManager.addStamp("yakitori_icon_stamp")
        }
    }
}
