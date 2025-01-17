package com.example.nuka2024_try.ui.stores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nuka2024_try.R
import org.w3c.dom.Text
import java.net.URL

class StoreAdapter(private val storeList: List<Store>) :
    RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    // 各アイテムの展開状態を保持するリスト
    private val expandedStates = MutableList(storeList.size) { false }

    class StoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.storeName)
        val descriptionTextView: TextView = view.findViewById(R.id.storeDescription)
        val storeImageView: ImageView = view.findViewById(R.id.storeImage)
        val expandedLayout: View = view.findViewById(R.id.expandedLayout)
        val toggleButton: TextView = view.findViewById(R.id.toggleButton) // 「詳細を見る/閉じる」用
        val industriesTextView: TextView = view.findViewById(R.id.storeIndustries)
        val companyTextView: TextView = view.findViewById(R.id.storeCompany)
        val addressTextView: TextView = view.findViewById(R.id.storeAddress)
        val websiteTextView: TextView = view.findViewById(R.id.storeWebsite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_store, parent, false)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = storeList[position]

        // 基本情報のセット
        holder.nameTextView.text = store.name
        holder.descriptionTextView.text = store.description
        holder.storeImageView.setImageResource(store.imageResId)
        holder.industriesTextView.text = "業種: ${store.industries}"
        holder.companyTextView.text = "会社の特徴: ${store.company_feature}"
        holder.addressTextView.text = "住所: ${store.address}"
        holder.websiteTextView.text = "ホームページ: ${store.website_url}"

        // 展開状態の管理
        val isExpanded = expandedStates[position]
        holder.expandedLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.toggleButton.text = if (isExpanded) "閉じる" else "詳細を見る"

        // タップ時の挙動
        holder.toggleButton.setOnClickListener {
            expandedStates[position] = !isExpanded // 状態を反転
            notifyItemChanged(position) // 該当アイテムを更新
        }
    }

    override fun getItemCount() = storeList.size
}
