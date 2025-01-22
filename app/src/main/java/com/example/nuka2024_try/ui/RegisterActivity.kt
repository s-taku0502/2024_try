package com.example.nuka2024_try.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.MainActivity
import com.example.nuka2024_try.R
//import com.google.firebase.auth.FirebaseAuth

// ユーザー登録画面を提供するアクティビティ
class RegisterActivity : AppCompatActivity() {

    // Firebase Authentication インスタンス
//    private lateinit var auth: FirebaseAuth
    // メールアドレス入力欄
    private lateinit var emailEditText: EditText
    // パスワード入力欄
    private lateinit var passwordEditText: EditText
    // 確認用パスワード入力欄（未使用だが今後使用予定）
    private lateinit var confirmPasswordEditText: EditText
    // 誕生日入力欄
    private lateinit var birthdayEditText: EditText
    // 性別選択用のラジオグループ
    private lateinit var genderRadioGroup: RadioGroup
    // 登録ボタン
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 各UIコンポーネントを初期化
        initializeViews()
        // Firebase のセットアップ
        setupFirebase()
        // 登録ボタンクリック時の処理を設定
        setupClickListeners()
    }

    // UIコンポーネントをレイアウトファイルから取得して初期化
    private fun initializeViews() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        birthdayEditText = findViewById(R.id.birthdayEditText)
        genderRadioGroup = findViewById(R.id.genderRadioGroup)
        registerButton = findViewById(R.id.registerButton)
    }

    // Firebase Authentication インスタンスの取得
    private fun setupFirebase() {
//        auth = FirebaseAuth.getInstance()
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

        // 入力チェックに失敗した場合は処理を中断
        if (!validateInput(email, password)) return

        // ユーザー登録を実行
//        registerUser(email, password)
    }

    // 入力内容を検証
    private fun validateInput(email: String, password: String): Boolean {
        // メールアドレスとパスワードが空の場合にエラーを表示
        if (email.isEmpty() || password.isEmpty()) {
            showToast("メールアドレスとパスワードを入力してください")
            return false
        }
        return true
    }

    // Firebase Authentication を使ってユーザーを登録
//    private fun registerUser(email: String, password: String) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    handleSuccessfulRegistration() // 登録成功時の処理
//                } else {
//                    // エラー時にエラーメッセージを表示
//                    showToast("登録失敗: ${task.exception?.message}")
//                }
//            }
//    }

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
