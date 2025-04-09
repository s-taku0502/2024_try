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
    // newsList は通常のお知らせのみのリスト
    private val newsList: List<NewsItem>,
    private val onClickRead: (NewsItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // ビュータイプの定義
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    // ヘッダー用 ViewHolder（単にタイトルを表示する）
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textHeader: TextView = itemView.findViewById(R.id.textHeader)
    }

    // 通常お知らせ用 ViewHolder（従来の NewsViewHolder）
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textEndDate: TextView = itemView.findViewById(R.id.textNewsEndDate)
        val textContent: TextView = itemView.findViewById(R.id.textNewsContent)
        val buttonRead: Button = itemView.findViewById(R.id.buttonRead)
    }

    // 各アイテムのビュータイプを返す
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    // getItemCount はヘッダー分 + 通常リストのサイズ
    override fun getItemCount(): Int = newsList.size + 1

    // onCreateViewHolder ではビュータイプに応じたレイアウトを inflate する
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            // ヘッダー用レイアウト (item_news_header.xml を新規作成する)
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)
            NewsViewHolder(view)
        }
    }

    // onBindViewHolder: ヘッダーと通常お知らせで異なるバインド処理を行う
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            // ヘッダー：リソースからタイトル文字列を取得するか、直接設定する
            holder.textHeader.text = holder.itemView.context.getString(R.string.title_news)
        } else if (holder is NewsViewHolder) {
            // 通常のお知らせの場合、実際のリストは position-1 で取得する
            val item = newsList[position - 1]
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
    }
}
