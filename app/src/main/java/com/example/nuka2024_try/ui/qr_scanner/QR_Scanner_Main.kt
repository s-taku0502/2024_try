package com.example.nuka2024_try.ui.qr_scanner

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.nuka2024_try.R
import com.example.nuka2024_try.ui.stamps.StampsFragment
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONArray

class QR_Scanner_Main : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        val qrButton = findViewById<Button>(R.id.qr_button)
        Log.d("activityQR","ActivityQR")

        // ボタンを押した際にQRスキャン画面に遷移
        qrButton.setOnClickListener {
            IntentIntegrator(this).apply {
                captureActivity = QRCodeCaptureActivity::class.java // QRスキャンアクティビティを設定
            }.initiateScan()
        }
    }

    // QRスキャン結果の処理
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("呼ばれた","call!!!")
        Log.d("ヨバレタ", data!!.data.toString())

        // QRコード読み取り結果
        if (resultCode == RESULT_OK && data != null) {
            val qrCodeResult = data.getStringExtra("SCAN_RESULT")?.toIntOrNull() ?: 3
            saveStamp(qrCodeResult)
            Log.d("stampleState",qrCodeResult.toString())
            // StampsFragment に渡すためのバンドルを作成
            val bundle = Bundle()
            bundle.putString("qrCodeResult", qrCodeResult.toString())

            // StampsFragment のインスタンスを作成してバンドルを設定
            val stampsFragment = StampsFragment()
            stampsFragment.arguments = bundle

            // FragmentTransaction を使って StampsFragment を表示
            supportFragmentManager.beginTransaction()
                .replace(R.id.stampContainer, stampsFragment) // レイアウト内の適切なコンテナに置き換え
                .commit()
        }
    }

    // スタンプをSharedPreferencesに保存する
    private fun saveStamp(index: Int) {
        val sharedPreferences = application.getSharedPreferences("Stamps", Context.MODE_PRIVATE)
        val currentData = loadNumberArray("stamps").toMutableSet()
        currentData.add(index)
        val jsonArray = JSONArray(currentData.toList())
        sharedPreferences.edit().putString("stamps", jsonArray.toString()).apply()
    }

    private fun loadNumberArray(key: String): List<Int>{
        val sharedPreferences: SharedPreferences = application.getSharedPreferences("Stamps",Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(key,null) ?:return emptyList()
        val jsonArray = JSONArray(jsonString)

        val numbers = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()){
            numbers.add((jsonArray.getInt(i)))
        }
        return  numbers
    }
}
