package com.example.nuka2024_try.ui.stores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nuka2024_try.R
import com.google.firebase.storage.FirebaseStorage

class StoreAdapter(private val storeList: List<Store>) :
    RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    // 展開状態のリスト
    private val expandedStates = MutableList(storeList.size) { false }

    class StoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.storeName)
        val descriptionTextView: TextView = view.findViewById(R.id.storeDescription)
        val storeImageView: ImageView = view.findViewById(R.id.storeImage)
        val expandedLayout: View = view.findViewById(R.id.expandedLayout)
        val toggleButton: TextView = view.findViewById(R.id.toggleButton)

        // 詳細表示
        val industryTextView: TextView = view.findViewById(R.id.storeIndustries)
        val featuresTextView: TextView = view.findViewById(R.id.storeFeatures)
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

        // 基本情報の設定
        holder.nameTextView.text = store.name
        holder.descriptionTextView.text = store.description
        holder.industryTextView.text = "業種: ${store.industry}"
        holder.featuresTextView.text = "会社の特徴: ${store.company_features}"
        holder.addressTextView.text = "住所: ${store.address}"
        holder.websiteTextView.text = "ホームページ: ${store.website_url}"

        // まずは必ず白い背景（プレースホルダー）をセットして、前の画像が残らないようにする
        holder.storeImageView.setImageResource(R.drawable.white_placeholder)

        // 画像の読み込み（placeholder と error に白い画像を指定）
        if (store.imageUrl.startsWith("gs://")) {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(store.imageUrl)
            storageRef.downloadUrl
                .addOnSuccessListener { uri ->
                    Glide.with(holder.itemView.context)
                        .load(uri)
                        .placeholder(R.drawable.white_placeholder)
                        .error(R.drawable.white_placeholder)
                        .into(holder.storeImageView)
                }
                .addOnFailureListener {
                    holder.storeImageView.setImageResource(R.drawable.white_placeholder)
                }
        } else {
            Glide.with(holder.itemView.context)
                .load(store.imageUrl)
                .placeholder(R.drawable.white_placeholder)
                .error(R.drawable.white_placeholder)
                .into(holder.storeImageView)
        }

        // 展開状態の管理
        val isExpanded = expandedStates[position]
        holder.expandedLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.toggleButton.text = if (isExpanded) "閉じる" else "詳細を見る"

        holder.toggleButton.setOnClickListener {
            expandedStates[position] = !isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = storeList.size
}
