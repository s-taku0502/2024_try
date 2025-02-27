package com.example.nuka2024_try.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.MainActivity
import com.example.nuka2024_try.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var editregisterEmail: EditText
    private lateinit var editregisterPassword: EditText
    // XML の ID は "editrejisterName"（スペルに注意）
    private lateinit var editregisterName: EditText
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 各ビューの初期化
        editregisterEmail = findViewById(R.id.editregisterEmail)
        editregisterPassword = findViewById(R.id.editregisterPassword)
        editregisterName = findViewById(R.id.editrejisterName)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val email = editregisterEmail.text.toString().trim()
            val password = editregisterPassword.text.toString().trim()
            val name = editregisterName.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password, name)
            } else {
                Toast.makeText(this, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, password: String, name: String) {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // ユーザー登録成功時の処理
                    val user = auth.currentUser
                    // 表示名（名前）の設定（任意）
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)

                    Toast.makeText(this, "登録完了: ${user?.email}", Toast.LENGTH_SHORT).show()

                    // 登録後はホーム画面(MainActivity)へ遷移
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "登録失敗: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
