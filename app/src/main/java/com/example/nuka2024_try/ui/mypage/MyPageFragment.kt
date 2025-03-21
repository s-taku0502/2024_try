package com.example.nuka2024_try.ui.mypage

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nuka2024_try.databinding.FragmentMypageBinding
import com.example.nuka2024_try.ui.auth.LoginActivity
import com.example.nuka2024_try.ui.auth.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    // Firebase関連
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) ログイン中のユーザーを取得
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // 未ログインならログイン画面へ
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        // 2) UIDを使って Firestore の usersコレクションを参照
        val uid = currentUser.uid               // AuthenticationのUID
        val userDocRef = db.collection("users").document(uid)

        // 3) Firestoreから「name」「email」などを取得して表示
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val currentName = document.getString("name") ?: "未設定"
                    val currentEmail = document.getString("email") ?: currentUser.email ?: "未設定"

                    // 画面上に表示
                    binding.textCurrentUserName.text = "ユーザー名: $currentName"
                    binding.textEmailAddress.text = "メールアドレス: $currentEmail"
                } else {
                    Toast.makeText(requireContext(), "ユーザー情報がありません", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "情報取得に失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        // --- 以下、ボタンの処理 ---

        // (A) ユーザー名の変更
        binding.buttonChangeUserName.setOnClickListener {
            val newName = binding.editNewUserName.text.toString()
            if (newName.isNotEmpty()) {
                // Firestoreで "name" フィールドを更新
                userDocRef.update("name", newName)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "ユーザー名を変更しました", Toast.LENGTH_SHORT).show()
                        // 画面上の表示も更新
                        binding.textCurrentUserName.text = "ユーザー名: $newName"
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "変更失敗: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "新しいユーザー名を入力してください", Toast.LENGTH_SHORT).show()
            }
        }

        // (B) ログアウト
        binding.buttonLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "ログアウトしました", Toast.LENGTH_SHORT).show()

            // ログイン画面へ遷移
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        // (C) アカウント削除ボタン
        binding.buttonDeleteAccount.setOnClickListener {
            // currentStampsの削除にメールアドレスが必要
            val userEmail = currentUser.email ?: ""
            showDeleteAccountDialog(uid, userEmail)
        }
    }

    /**
     * アカウント削除確認ダイアログ
     */
    private fun showDeleteAccountDialog(uid: String, userEmail: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("本当に削除しますか？")
        builder.setMessage("この操作は取り消せません。")

        // 「はい」ボタン
        builder.setPositiveButton("はい") { dialog, _ ->
            deleteAccount(uid, userEmail)
            dialog.dismiss()
        }

        // 「いいえ」ボタン
        builder.setNegativeButton("いいえ") { dialog, _ ->
            dialog.dismiss() // 何もしないで閉じる
        }

        builder.create().show()
    }

    /**
     * アカウント削除処理
     * 1) currentStamps からスタンプ情報削除（メールアドレスがドキュメントID）
     * 2) usersコレクションのユーザードキュメント削除（UIDがドキュメントID）
     * 3) FirebaseAuthのユーザーを削除
     * 4) RegisterActivityへ遷移
     */
    private fun deleteAccount(uid: String, userEmail: String) {
        val currentUser = auth.currentUser ?: return

        // 1) currentStamps から削除
        db.collection("currentStamps").document(userEmail)
            .delete()
            .addOnSuccessListener {
                // 2) usersコレクションのドキュメント削除
                db.collection("users").document(uid)
                    .delete()
                    .addOnSuccessListener {
                        // 3) FirebaseAuth のアカウント削除
                        currentUser.delete()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "アカウントを削除しました", Toast.LENGTH_SHORT).show()

                                    // 4) RegisterActivityへ遷移
                                    val intent = Intent(requireContext(), RegisterActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                } else {
                                    Toast.makeText(requireContext(), "アカウント削除に失敗しました", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "ユーザードキュメント削除失敗: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "スタンプ情報削除に失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
