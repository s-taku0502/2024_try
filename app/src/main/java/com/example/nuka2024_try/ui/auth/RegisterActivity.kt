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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var editregisterEmail: EditText
    private lateinit var editregisterPassword: EditText
    private lateinit var editregisterName: EditText
    private lateinit var buttonRegister: Button

    // パスワード表示/非表示を切り替える ImageView
    private lateinit var imageTogglePasswordRegister: ImageView
    // 「すでにアカウントをお持ちですか？ ログインはこちら」のテキスト
    private lateinit var textGotoLogin: TextView

    // 新たに追加
    private lateinit var editAge: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioMale: RadioButton
    private lateinit var radioFemale: RadioButton

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

        // 追加したUI要素
        editAge = findViewById(R.id.editAge)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioMale = findViewById(R.id.radioMale)
        radioFemale = findViewById(R.id.radioFemale)

        // 「すでにアカウントをお持ちですか？ ログインはこちら」の設定
        setupGotoLoginText()

        // パスワード表示/非表示のアイコンタップで切り替え
        imageTogglePasswordRegister.setOnClickListener {
            togglePasswordVisibility()
        }

        // 新規登録ボタン
        buttonRegister.setOnClickListener {
            val email = editregisterEmail.text.toString().trim()
            val password = editregisterPassword.text.toString().trim()
            val name = editregisterName.text.toString().trim()

            // 年齢（数値に変換）
            val ageStr = editAge.text.toString().trim()
            val age = if (ageStr.isNotEmpty()) {
                ageStr.toInt()
            } else {
                0
            }

            // 性別
            val gender = when (radioGroupGender.checkedRadioButtonId) {
                R.id.radioMale -> "男性"
                R.id.radioFemale -> "女性"
                else -> "その他"
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password, name, age, gender)
            } else {
                Toast.makeText(this, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 「すでにアカウントをお持ちですか？ ログインはこちら」の装飾とクリック設定
    private fun setupGotoLoginText() {
        val fullText = "すでにアカウントをお持ちですか？　 ログインはこちら"
        val spannable = SpannableStringBuilder(fullText)

        val target = "ログインはこちら"
        val startIndex = fullText.indexOf(target)
        val endIndex = startIndex + target.length

        if (startIndex != -1) {
            // 下線を付与
            spannable.setSpan(UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            // クリックイベントを付与
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // LoginActivity へ遷移
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textGotoLogin.text = spannable
        textGotoLogin.movementMethod = LinkMovementMethod.getInstance()
    }

    // パスワードの表示/非表示切替
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            editregisterPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            imageTogglePasswordRegister.setImageResource(R.drawable.eye_closed)
            isPasswordVisible = false
        } else {
            editregisterPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imageTogglePasswordRegister.setImageResource(R.drawable.eye_open)
            isPasswordVisible = true
        }
        // カーソルを末尾に移動
        editregisterPassword.setSelection(editregisterPassword.text.length)
    }

    /**
     * ユーザー登録に成功した後、Firestore の "users" コレクションに以下のフィールドでユーザー情報を追加する:
     *   - "name": ユーザー名
     *   - "email": メールアドレス
     *   - "stamps": 空の配列
     *   - "age": 年齢（数値）
     *   - "gender": 性別（文字列: "male", "female", など）
     */
    private fun registerUser(email: String, password: String, name: String, age: Int, gender: String) {
        val auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    // 表示名を設定（任意）
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)

                    Toast.makeText(this, "登録完了: ${user?.email}", Toast.LENGTH_SHORT).show()

                    // Firestore へユーザー情報を書き込む
                    user?.uid?.let { uid ->
                        val db = Firebase.firestore
                        val userData = mapOf(
                            "name" to name,
                            "email" to email,
                            "age" to age,
                            "gender" to gender
                        )
                        db.collection("users")
                            .document(uid)
                            .set(userData)
                            .addOnSuccessListener {
                                // 登録完了後にホーム画面へ遷移
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Firestore書き込み失敗: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } ?: run {
                        // user が null の場合はそのままホーム画面へ
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this, "登録失敗: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
