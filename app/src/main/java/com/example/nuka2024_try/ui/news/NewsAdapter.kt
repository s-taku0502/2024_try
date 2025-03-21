package com.example.nuka2024_try.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nuka2024_try.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NewsAdapter(
    private val newsList: List<NewsItem>,
    private val onClickRead: (NewsItem) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEndDate: TextView = itemView.findViewById(R.id.textNewsEndDate)
        val textContent: TextView = itemView.findViewById(R.id.textNewsContent)
        val buttonRead: Button = itemView.findViewById(R.id.buttonRead)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = newsList[position]
        holder.textEndDate.text = item.endDate
        holder.textContent.text = item.content

        val currentUid = Firebase.auth.currentUser?.uid
        val isRead = currentUid != null && item.readUsers.contains(currentUid)
        holder.buttonRead.isEnabled = !isRead
        holder.buttonRead.text = if (isRead) "既読済み" else "既読にする"

        holder.buttonRead.setOnClickListener {
            onClickRead(item)
        }
    }

    override fun getItemCount() = newsList.size
}
