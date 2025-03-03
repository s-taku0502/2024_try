package com.example.nuka2024_try.ui.contact

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.nuka2024_try.R
import com.example.nuka2024_try.ui.auth.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContactFragment : Fragment() {

    private lateinit var spinnerContactType: Spinner
    private lateinit var editContactUserName: EditText
    private lateinit var editContactEmail: EditText
    private lateinit var editContactContent: EditText
    private lateinit var buttonSendContact: Button

    // Firestoreインスタンス
    private val db = Firebase.firestore

    private val TAG = "ContactFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_contact.xml を inflate して返す
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ログインユーザーの状態をチェック
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            Log.e(TAG, "User is not logged in! Redirecting to LoginActivity.")
            // Fragment から Activity を呼び出してログイン画面へ遷移
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        } else {
            Log.d(TAG, "User is logged in: ${currentUser.email}")
        }

        // View の紐づけ
        spinnerContactType = view.findViewById(R.id.spinnerContactType)
        editContactUserName = view.findViewById(R.id.editContactUserName)
        editContactEmail = view.findViewById(R.id.editContactEmail)
        editContactContent = view.findViewById(R.id.editContactContent)
        buttonSendContact = view.findViewById(R.id.buttonSendContact)

        // Spinner に表示する候補を設定
        setupSpinner()

        // 送信ボタンの処理
        buttonSendContact.setOnClickListener {
            sendContact()
        }
    }

    /**
     * Spinnerに選択肢をセット
     */
    private fun setupSpinner() {
        val contactTypes = listOf(
            "選択",
            "パスワードを忘れた",
            "スタンプらり～に関するお問い合わせ",
            "会員の出店について",
            "活動に関するお問い合わせ",
            "個人情報に関するお問い合わせ",
            "当アプリへのご意見・ご要望",
            "その他"
        )
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            contactTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerContactType.adapter = adapter
        Log.d(TAG, "Spinner setup complete.")
    }

    /**
     * 送信ボタン押下時の処理
     * Firestoreの "contacts" コレクションへ保存する
     */
    private fun sendContact() {
        val type = spinnerContactType.selectedItem.toString()
        val userName = editContactUserName.text.toString().trim()
        val email = editContactEmail.text.toString().trim()
        val content = editContactContent.text.toString().trim()

        Log.d(TAG, "sendContact: type=$type, userName=$userName, email=$email, content=$content")

        // 必須項目チェック
        if (email.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "メールアドレスとお問い合わせ内容を入力してください", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "sendContact: 必須項目が入力されていません")
            return
        }

        // Firestoreに保存するデータ
        val contactData = hashMapOf(
            "type" to type,
            "userName" to userName,
            "email" to email,
            "content" to content,
            "timestamp" to FieldValue.serverTimestamp() // サーバー時刻を付加
        )

        db.collection("contacts")
            .add(contactData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "送信が完了しました", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "sendContact: 送信成功")
                // フォームをリセット
                resetForm()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "送信に失敗しました: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "sendContact: 送信失敗", e)
            }
    }

    /**
     * フォーム内容をクリア
     */
    private fun resetForm() {
        spinnerContactType.setSelection(0)
        editContactUserName.setText("")
        editContactEmail.setText("")
        editContactContent.setText("")
        Log.d(TAG, "resetForm: フォームリセット完了")
    }
}
