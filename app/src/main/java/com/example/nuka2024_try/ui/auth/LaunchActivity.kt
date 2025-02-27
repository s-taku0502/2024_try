package com.example.nuka2024_try.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.R

class LaunchActivity : AppCompatActivity() {

    private lateinit var buttonGoRegister: Button
    private lateinit var buttonGoLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch) // 2つのボタンがあるレイアウト

        buttonGoRegister = findViewById(R.id.buttonGoRegister)
        buttonGoLogin = findViewById(R.id.buttonGoLogin)

        buttonGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        buttonGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
