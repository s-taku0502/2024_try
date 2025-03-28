package com.example.nuka2024_try.ui.news

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.databinding.ActivityNewsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.ktx.Firebase

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private val db = Firebase.firestore
    private lateinit var currentUid: String

    private lateinit var newsAdapter: NewsAdapter
    private var newsList: MutableList<NewsItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerNews.layoutManager = LinearLayoutManager(this)

        currentUid = Firebase.auth.currentUser?.uid ?: run {
            Toast.makeText(this, "ログイン状態が保持されていません", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        newsAdapter = NewsAdapter(newsList) { newsItem ->
            markAsRead(newsItem.id, currentUid)
        }
        binding.recyclerNews.adapter = newsAdapter

        // リアルタイムでニュース一覧を更新
        observeNewsData(currentUid)
    }

    /**
     * Firestore の "news" コレクションをリアルタイム監視し、ニュース一覧を更新する。
     */
    private fun observeNewsData(currentUid: String) {
        db.collection("news")
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Toast.makeText(this, "ニュース取得失敗: ${error.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                if (querySnapshot != null) {
                    newsList.clear()
                    for (doc in querySnapshot.documents) {
                        val id = doc.id
                        val content = doc.getString("content") ?: ""
                        val endDate = doc.getString("endDate") ?: ""
                        val organization = doc.getString("organization") ?: ""
                        val readUsers = doc.get("readUsers") as? List<String> ?: emptyList()
                        val newsItem = NewsItem(id, content, endDate, organization, readUsers)
                        newsList.add(newsItem)
                    }
                    newsAdapter.notifyDataSetChanged()
                }
            }
    }

    /**
     * 指定したニュースを既読にする。ログイン中ユーザーの UID を readUsers に追加。
     */
    private fun markAsRead(newsId: String, currentUid: String) {
        db.collection("news").document(newsId)
            .update("readUsers", FieldValue.arrayUnion(currentUid))
            .addOnSuccessListener {
                Toast.makeText(this, "既読にしました", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "既読処理失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
