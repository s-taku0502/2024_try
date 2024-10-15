package com.example.nuka2024_try.ui.stores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nuka2024_try.R
import com.example.nuka2024_try.ui.stores.Store

class StoresAdapter(
    private val stores: List<Store>,
    private val onStoreClick: (Store) -> Unit // クリック時の動作を引数で受け取る
) : RecyclerView.Adapter<StoresAdapter.StoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store = stores[position]
        holder.bind(store)
        holder.itemView.setOnClickListener {
            onStoreClick(store) // クリック時にコールバック関数を実行
        }
    }

    class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(store: Store) {
            Glide.with(itemView.context)
                .load(store.imageUrl)
                .into(imageView)
        }
    }

    override fun getItemCount(): Int = stores.size
}
