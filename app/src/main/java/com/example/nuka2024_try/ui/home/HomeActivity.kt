package com.example.nuka2024_try.ui.home

import com.example.nuka2024_try.R// パッケージ名は適宜変更してください
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home) // レイアウトファイルを設定
    }
}
