package com.example.nuka2024_try.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.nuka2024_try.MainActivity
import com.example.nuka2024_try.R
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var editregisterEmail: EditText
    private lateinit var editregisterPassword: EditText
    private lateinit var editregisterName: EditText
    private lateinit var buttonRegister: Button

    // 表示/非表示を切り替える ImageView
    private lateinit var imageTogglePasswordRegister: ImageView
    // 「すでにアカウントをお持ちですか？ ログインはこちら」
    private lateinit var textGotoLogin: TextView

    // パスワード表示/非表示フラグ
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 各ビューの初期化
        editregisterEmail = findViewById(R.id.editregisterEmail)
        editregisterPassword = findViewById(R.id.editregisterPassword)
        editregisterName = findViewById(R.id.editrejisterName)
        buttonRegister = findViewById(R.id.buttonRegister)
        imageTogglePasswordRegister = findViewById(R.id.imageTogglePasswordRegister)
        textGotoLogin = findViewById(R.id.textGotoLogin)

        // 「すでにアカウントをお持ちですか？ ログインはこちら」の部分を設定
        setupGotoLoginText()

        // パスワード表示/非表示のアイコンをタップで切り替え
        imageTogglePasswordRegister.setOnClickListener {
            togglePasswordVisibility()
        }

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

    // 「すでにアカウントをお持ちですか？  ログインはこちら」の装飾・クリック設定
    private fun setupGotoLoginText() {
        // 全体の文章に余白を含めて、「ログインはこちら」部分を探す
        val fullText = "すでにアカウントをお持ちですか？　 ログインはこちら"
        val spannable = SpannableStringBuilder(fullText)

        val target = "ログインはこちら"
        val startIndex = fullText.indexOf(target)
        val endIndex = startIndex + target.length

        if (startIndex != -1) {
            // 下線を付与
            spannable.setSpan(
                UnderlineSpan(),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // クリックイベントを付与
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // LoginActivity へ遷移
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // 戻る操作で二重になるのを防ぐ場合はfinish()してもOK
                }
            }
            spannable.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        textGotoLogin.text = spannable
        textGotoLogin.movementMethod = LinkMovementMethod.getInstance()
    }

    // パスワードの表示/非表示を切り替え
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // 非表示
            editregisterPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            imageTogglePasswordRegister.setImageResource(R.drawable.eye_closed)
            isPasswordVisible = false
        } else {
            // 表示
            editregisterPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imageTogglePasswordRegister.setImageResource(R.drawable.eye_open)
            isPasswordVisible = true
        }
        // カーソルを末尾に移動
        editregisterPassword.setSelection(editregisterPassword.text.length)
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
