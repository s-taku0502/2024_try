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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    // Firebase
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    // ログインユーザー
    private var currentUid: String? = null

    // RecyclerView 関連
    private lateinit var newsAdapter: NewsAdapter
    private var newsList: MutableList<NewsItem> = mutableListOf()

    // リスナーを解除するための変数
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

        // RecyclerView のセットアップ
        binding.recyclerNews.layoutManager = LinearLayoutManager(requireContext())

        // ログインユーザー情報を取得
        currentUid = auth.currentUser?.uid

        // Adapter のセットアップ
        newsAdapter = NewsAdapter(newsList) { newsItem ->
            // 既読処理
            currentUid?.let { uid ->
                markAsRead(newsItem.id, uid)
            } ?: run {
                Toast.makeText(requireContext(), "ユーザー情報が取得できていません", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerNews.adapter = newsAdapter

        // Firestore からニュースをリアルタイムで取得
        observeNewsData()
    }

    override fun onStop() {
        super.onStop()
        // リスナー解除
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    /**
     * news コレクションを endDate の降順で取得し、リストに反映
     */
    private fun observeNewsData() {
        listenerRegistration = db.collection("news")
            .orderBy("endDate", Query.Direction.DESCENDING)  // ★ ここがポイント
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Log.e("NewsFragment", "ニュース取得失敗: ${error.message}")
                    return@addSnapshotListener
                }
                querySnapshot?.let { snapshot ->
                    // リストをいったんクリアして更新
                    newsList.clear()
                    for (doc in snapshot.documents) {
                        val id = doc.id
                        val content = doc.getString("content") ?: ""
                        val endDate = doc.getString("endDate") ?: ""
                        val organization = doc.getString("organization") ?: ""
                        val readUsers = doc.get("readUsers") as? List<String> ?: emptyList()

                        val newsItem = NewsItem(
                            id = id,
                            content = content,
                            endDate = endDate,
                            organization = organization,
                            readUsers = readUsers
                        )
                        newsList.add(newsItem)
                    }
                    // リストの変更をアダプターに通知
                    newsAdapter.notifyDataSetChanged()
                }
            }
    }

    /**
     * 既読処理
     */
    private fun markAsRead(newsId: String, uid: String) {
        db.collection("news").document(newsId)
            .update("readUsers", FieldValue.arrayUnion(uid))
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "既読にしました", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "既読処理失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
