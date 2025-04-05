package com.example.nuka2024_try.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.databinding.FragmentNewsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    // currentUid はログイン済みの場合にのみ設定（null の場合は既読処理を実施しない）
    private var currentUid: String? = null

    private lateinit var newsAdapter: NewsAdapter
    private var newsList: MutableList<NewsItem> = mutableListOf()

    // Firestore のリスナー解除用
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerNews.layoutManager = LinearLayoutManager(requireContext())

        // 現在のユーザー情報を取得してログ出力
        currentUid = auth.currentUser?.uid
        if (currentUid != null) {
            Log.d("NewsFragment", "ログイン中: $currentUid")
        } else {
            Log.d("NewsFragment", "ログインできていない")
            Toast.makeText(requireContext(), "ログイン状態が保持されていません", Toast.LENGTH_SHORT).show()
        }

        // アダプタ設定：ボタン押下時に currentUid が取得できていれば即座に既読処理を実施
        newsAdapter = NewsAdapter(newsList) { newsItem ->
            currentUid?.let { uid ->
                markAsRead(newsItem.id, uid)
            } ?: run {
                Toast.makeText(requireContext(), "ユーザー情報が取得できていません", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerNews.adapter = newsAdapter

        // Firestore のニュースデータ監視を開始
        observeNewsData()
    }

    override fun onStart() {
        super.onStart()
        // AuthStateListener で認証状態の変化を監視し、ログ出力する
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                currentUid = user.uid
                Log.d("NewsFragment", "AuthStateListener: ログイン中: $currentUid")
            } else {
                Log.d("NewsFragment", "AuthStateListener: ログインできていない")
                Toast.makeText(requireContext(), "ログアウトしました", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    /**
     * Firestore の "news" コレクションをリアルタイム監視し、ニュース一覧を更新する
     */
    private fun observeNewsData() {
        listenerRegistration = db.collection("news")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("NewsFragment", "ニュース取得失敗: ${error.message}")
                    return@addSnapshotListener
                }
                querySnapshot?.let { snapshot ->
                    newsList.clear()
                    for (doc in snapshot.documents) {
                        val id = doc.id
                        val content = doc.getString("content") ?: ""
                        val endDate = doc.getString("endDate") ?: ""
                        val organization = doc.getString("organization") ?: ""
                        // readUsers が存在しない場合は空リストとする
                        val readUsers = doc.get("readUsers") as? List<String> ?: emptyList()
                        val newsItem = NewsItem(id, content, endDate, organization, readUsers)
                        newsList.add(newsItem)
                    }
                    newsAdapter.notifyDataSetChanged()
                }
            }
    }

    /**
     * 指定したニュースを既読にする（ログイン中ユーザーの UID を readUsers に追加）
     */
    private fun markAsRead(newsId: String, uid: String) {
        db.collection("news").document(newsId)
            .update("readUsers", FieldValue.arrayUnion(uid))
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "既読にしました", Toast.LENGTH_SHORT).show()
                Log.d("NewsFragment", "既読処理成功: $newsId に $uid を追加")
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "既読処理失敗: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("NewsFragment", "既読処理失敗: ${e.message}")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listenerRegistration?.remove()
        listenerRegistration = null
        _binding = null
    }
}
