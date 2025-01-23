package com.example.nuka2024_try.ui.qr_scanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.nuka2024_try.MainActivity
import com.example.nuka2024_try.R
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class PortraitCaptureActivity : CaptureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // onCreateでは setContentView() を呼ばず、ZXing本体の仕組みに任せる
    }

    /**
     * カスタムレイアウトを使ってスキャナ画面を初期化
     * ※戻り値が DecoratedBarcodeView 型である必要がある
     */
    override fun initializeContent(): DecoratedBarcodeView {
        // カスタムレイアウトを設定
        setContentView(R.layout.custom_capture_layout)

        // レイアウトからBarcodeViewを取得
        val barcodeView = findViewById<DecoratedBarcodeView>(R.id.zxing_barcode_scanner)

        // 「Homeへ移動」ボタンを押したらHome画面に遷移する
        val buttonHome = findViewById<Button>(R.id.button_home)
        buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // DecoratedBarcodeView を返す
        return barcodeView
    }
}
