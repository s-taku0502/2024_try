package com.example.nuka2024_try.ui.qr_scanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.R
import com.example.nuka2024_try.ui.stamps.StampsFragment
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
/*
class QRCodeCaptureActivity : CaptureActivity() {
    private lateinit var capture: CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_qrcode)

        val barcodeScannerView = findViewById<DecoratedBarcodeView>(R.id.qrcode_reader)

        capture = CaptureManager(this, barcodeScannerView)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.decode() // QRコードのスキャンを開始
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

 */
/*
class QRCodeCaptureActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            val qrCode = result.contents  // QRコードの内容を取得

            // StampsFragmentのインスタンスにアクセスしてスタンプを追加
            val fragment = supportFragmentManager.findFragmentById(R.id.stampContainer) as? StampsFragment
            fragment?.addStamp(qrCode, findViewById(R.id.stampContainer))  // QRコードをフラグメントに渡す
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
}

/*
class QRCodeCaptureActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            val qrCode = result.contents  // QRコードの内容を取得

            // StampsFragmentのインスタンスにアクセスしてスタンプを追加
            val fragment = supportFragmentManager.findFragmentById(R.id.stampContainer) as? StampsFragment
            fragment?.addStamp(qrCode, findViewById(R.id.stampContainer))  // QRコードをフラグメントに渡す
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
*/

 */

class QRCodeCaptureActivity : AppCompatActivity() {
    private lateinit var capture: CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_qrcode)

        val barcodeScannerView = findViewById<DecoratedBarcodeView>(R.id.qrcode_reader)

        capture = CaptureManager(this, barcodeScannerView)
        capture.initializeFromIntent(intent, savedInstanceState)
        capture.decode() // QRコードのスキャンを開始
    }

    override fun onResume() {
        super.onResume()
        capture.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture.onDestroy()
    }

    // QRコードスキャン結果を処理する
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            val qrCode = result.contents  // QRコードの内容を取得

            // StampsFragmentのインスタンスにアクセスしてスタンプを追加
            val fragment = supportFragmentManager.findFragmentById(R.id.stampContainer) as? StampsFragment
            fragment?.addStamp(qrCode)
            //fragment?.addStamp(qrCode, findViewById(R.id.stampContainer))  // QRコードをフラグメントに渡す
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
