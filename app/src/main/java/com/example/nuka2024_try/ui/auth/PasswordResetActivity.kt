package com.example.nuka2024_try.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var editResetEmail: EditText
    private lateinit var buttonSendReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        editResetEmail = findViewById(R.id.editResetEmail)
        buttonSendReset = findViewById(R.id.buttonSendReset)

        buttonSendReset.setOnClickListener {
            val email = editResetEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                sendPasswordResetEmail(email)
            } else {
                Toast.makeText(this, "メールアドレスを入力してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Firebase Authentication のパスワードリセットメールを送信
     */
    private fun sendPasswordResetEmail(email: String) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "パスワード再設定メールを送信しました", Toast.LENGTH_SHORT).show()
                    finish() // 画面を閉じる
                } else {
                    Toast.makeText(this, "送信に失敗しました: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
