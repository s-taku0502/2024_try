package com.example.nuka2024_try.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.MainActivity
import com.example.nuka2024_try.R
//import com.google.firebase.auth.FirebaseAuth

// ユーザー登録画面を提供するアクティビティ
class RegisterActivity : AppCompatActivity() {

    // Firebase Authentication インスタンス（必要に応じて利用）
    // private lateinit var auth: FirebaseAuth

    // メールアドレス入力欄
    private lateinit var emailEditText: EditText
    // パスワード入力欄
    private lateinit var passwordEditText: EditText
    // 名前入力欄（XMLでは editrejisterName になっています）
    private lateinit var nameEditText: EditText
    // 登録ボタン
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 各UIコンポーネントを初期化
        initializeViews()
        // Firebase のセットアップ（必要に応じて）
        setupFirebase()
        // 登録ボタンクリック時の処理を設定
        setupClickListeners()
    }

    // UIコンポーネントをレイアウトファイルから取得して初期化
    private fun initializeViews() {
        emailEditText = findViewById(R.id.editregisterEmail)
        passwordEditText = findViewById(R.id.editregisterPassword)
        // XML上のIDは "editrejisterName" になっているので注意
        nameEditText = findViewById(R.id.editrejisterName)
        registerButton = findViewById(R.id.buttonRegister)
    }

    // Firebase Authentication インスタンスの取得（必要に応じて）
    private fun setupFirebase() {
        // auth = FirebaseAuth.getInstance()
    }

    // ボタンのクリックイベントを設定
    private fun setupClickListeners() {
        registerButton.setOnClickListener {
            handleRegistration() // 登録処理を開始
        }
    }

    // 入力された情報を取得して登録を実行
    private fun handleRegistration() {
        val email = emailEditText.text.toString().trim() // メールアドレスを取得
        val password = passwordEditText.text.toString().trim() // パスワードを取得
        val name = nameEditText.text.toString().trim() // 名前を取得

        // 入力チェックに失敗した場合は処理を中断
        if (!validateInput(email, password)) return

        // ユーザー登録を実行（FirebaseAuthを使用する場合）
        // registerUser(email, password)

        // 仮に登録成功時の処理
        handleSuccessfulRegistration()
    }

    // 入力内容を検証
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("メールアドレスとパスワードを入力してください")
            return false
        }
        return true
    }

    // Firebase Authentication を使ってユーザーを登録（必要に応じて）
    /*
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    handleSuccessfulRegistration()
                } else {
                    showToast("登録失敗: ${task.exception?.message}")
                }
            }
    }
    */

    // 登録成功時の処理
    private fun handleSuccessfulRegistration() {
        showToast("登録成功！")
        navigateToMain() // メイン画面に遷移
    }

    // メイン画面に遷移
    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // 現在のアクティビティを終了
    }

    // トーストでメッセージを表示
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
