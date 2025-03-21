package com.example.nuka2024_try.ui.news

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nuka2024_try.databinding.ActivityNewsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerNews.layoutManager = LinearLayoutManager(this)

        // onCreate内で currentUid を取得する
        val currentUid = Firebase.auth.currentUser?.uid
        if (currentUid == null) {
            Toast.makeText(this, "ログイン状態が保持されていません", Toast.LENGTH_SHORT).show()
            // 必要ならここでログイン画面にリダイレクト
            finish()
            return
        }

        loadNewsData(currentUid)
    }

    private fun loadNewsData(currentUid: String) {
        db.collection("news")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val newsList = querySnapshot.documents.map { doc ->
                    val id = doc.id
                    val content = doc.getString("content") ?: ""
                    val endDate = doc.getString("endDate") ?: ""
                    val organization = doc.getString("organization") ?: ""
                    val readUsers = doc.get("readUsers") as? List<String> ?: emptyList()
                    NewsItem(id, content, endDate, organization, readUsers)
                }
                binding.recyclerNews.adapter = NewsAdapter(newsList) { newsItem ->
                    markAsRead(newsItem.id, currentUid)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "ニュース取得失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun markAsRead(newsId: String, currentUid: String) {
        db.collection("news").document(newsId)
            .update("readUsers", FieldValue.arrayUnion(currentUid))
            .addOnSuccessListener {
                Toast.makeText(this, "既読にしました", Toast.LENGTH_SHORT).show()
                loadNewsData(currentUid)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "既読処理失敗: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
