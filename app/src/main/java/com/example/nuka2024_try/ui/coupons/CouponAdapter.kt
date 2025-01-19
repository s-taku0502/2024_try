package com.example.nuka2024_try.ui.coupons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nuka2024_try.R

class CouponAdapter(private val coupons: List<Coupon>) :
    RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeName: TextView = itemView.findViewById(R.id.storeName)
        val details: TextView = itemView.findViewById(R.id.couponDetails)
        val discount: TextView = itemView.findViewById(R.id.couponExpiry)
        val useButton: Button = itemView.findViewById(R.id.useCouponButton) // ボタンを追加
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coupon_item, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]
        holder.storeName.text = coupon.storeName
        holder.details.text = coupon.details
        holder.discount.text = coupon.discount

        // ボタンの初期状態
        holder.useButton.text = "クーポンを利用する"
        holder.useButton.isEnabled = true

        // ボタンクリック時の動作
        holder.useButton.setOnClickListener {
            holder.useButton.text = "使用済み"
            holder.useButton.isEnabled = false // ボタンを無効化
        }
    }

    override fun getItemCount(): Int = coupons.size
}
