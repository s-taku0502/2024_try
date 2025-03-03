package com.example.nuka2024_try.ui.auth

import android.content.Intent
import android.content.SharedPreferences
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var editloginEmail: EditText
    private lateinit var editloginPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textForgotPassword: TextView
    private lateinit var textRegister: TextView
    private lateinit var imageTogglePassword: ImageView

    private var isPasswordVisible = false
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Viewの紐づけ
        editloginEmail = findViewById(R.id.editloginEmail)
        editloginPassword = findViewById(R.id.editloginPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textForgotPassword = findViewById(R.id.textForgotPassword)
        textRegister = findViewById(R.id.textRegister)
        imageTogglePassword = findViewById(R.id.imageTogglePassword)

        // SharedPreferences の初期化
        sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        loadSavedCredentials()

        // パスワード表示/非表示切り替え
        imageTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        // ログインボタン
        buttonLogin.setOnClickListener {
            val email = editloginEmail.text.toString().trim()
            val password = editloginPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
            }
        }

        // 「パスワードを忘れた場合は～」のテキスト装飾を設定
        setupForgotPasswordText()

        // 「アカウントをお持ちでない場合は～」のテキスト装飾を設定
        setupRegisterText()
    }

    /**
     * 「パスワードを忘れた場合は、  こちら  より再設定の申請をしてください。」を部分装飾し、
     * タップ時にパスワード再設定画面(PasswordResetActivity)へ遷移する
     */
    private fun setupForgotPasswordText() {
        val fullText = "パスワードを忘れた場合は、  こちら  より再設定の申請をしてください。"
        val spannable = SpannableStringBuilder(fullText)

        val target = "こちら"
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
            // クリック時の動作
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // パスワード再設定画面へ遷移
                    val intent = Intent(this@LoginActivity, PasswordResetActivity::class.java)
                    startActivity(intent)
                }
            }
            spannable.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        textForgotPassword.text = spannable
        textForgotPassword.movementMethod = LinkMovementMethod.getInstance()
    }

    /**
     * 「アカウントをお持ちでない場合は、  こちら  より登録してください。」を部分装飾
     */
    private fun setupRegisterText() {
        val fullText = "アカウントをお持ちでない場合は、  こちら  より登録してください。"
        val spannable = SpannableStringBuilder(fullText)

        val target = "こちら"
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
            // クリック時の動作を付与
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // RegisterActivity へ遷移
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
            spannable.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        textRegister.text = spannable
        textRegister.movementMethod = LinkMovementMethod.getInstance()
    }

    // Firebase Auth でログイン処理
    private fun loginUser(email: String, password: String) {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "ログイン成功: ${user?.email}", Toast.LENGTH_SHORT).show()
                    saveCredentials(email, password)

                    // ログイン後はホーム画面(MainActivity)へ遷移
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "ログイン失敗: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // パスワードの表示・非表示を切り替える
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // 非表示にする
            editloginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            imageTogglePassword.setImageResource(R.drawable.eye_closed)
            isPasswordVisible = false
        } else {
            // 表示にする
            editloginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imageTogglePassword.setImageResource(R.drawable.eye_open)
            isPasswordVisible = true
        }
        // カーソルを文末に移動
        editloginPassword.setSelection(editloginPassword.text.length)
    }

    // SharedPreferencesに保存してあるメール＆パスワードを読み込んで表示
    private fun loadSavedCredentials() {
        val savedEmail = sharedPref.getString("email", "")
        val savedPassword = sharedPref.getString("password", "")
        editloginEmail.setText(savedEmail)
        editloginPassword.setText(savedPassword)
    }

    // ログイン成功時にメール＆パスワードを保存
    private fun saveCredentials(email: String, password: String) {
        val editor = sharedPref.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }
}
