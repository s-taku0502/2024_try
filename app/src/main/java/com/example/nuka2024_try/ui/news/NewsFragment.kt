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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore

    private lateinit var currentUid: String
    private lateinit var newsAdapter: NewsAdapter
    private var newsList: MutableList<NewsItem> = mutableListOf()

    // リスナー解除用
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerNews.layoutManager = LinearLayoutManager(requireContext())

        // ログインユーザーのチェック
        currentUid = Firebase.auth.currentUser?.uid ?: run {
            Toast.makeText(requireContext(), "ログイン状態が保持されていません", Toast.LENGTH_SHORT).show()
            // 必要に応じて、別画面へ遷移など
            return
        }

        // アダプタ設定
        newsAdapter = NewsAdapter(newsList) { newsItem ->
            markAsRead(newsItem.id, currentUid)
        }
        binding.recyclerNews.adapter = newsAdapter

        // Firestore のニュースコレクションをリアルタイム監視
        observeNewsData(currentUid)
    }

    /**
     * Firestore の "news" コレクションをリアルタイム監視し、ニュース一覧を更新する
     */
    private fun observeNewsData(currentUid: String) {
        listenerRegistration = db.collection("news")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    // ユーザーが既にログアウトしている場合はエラーをスキップ
                    if (Firebase.auth.currentUser == null) {
                        Log.d("NewsFragment", "ユーザーがログアウトしているため、スナップショットエラーを無視します: ${error.message}")
                    } else {
                        Toast.makeText(requireContext(), "ニュース取得失敗: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    newsList.clear()
                    for (doc in querySnapshot.documents) {
                        val id = doc.id
                        val content = doc.getString("content") ?: ""
                        val endDate = doc.getString("endDate") ?: ""
                        val organization = doc.getString("organization") ?: ""
                        // 型安全にするなら filterIsInstance<String>() を使用する方法もあり
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
    private fun markAsRead(newsId: String, currentUid: String) {
        db.collection("news").document(newsId)
            .update("readUsers", FieldValue.arrayUnion(currentUid))
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "既読にしました", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "既読処理失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // リスナー解除
        listenerRegistration?.remove()
        listenerRegistration = null
        _binding = null
    }
}
