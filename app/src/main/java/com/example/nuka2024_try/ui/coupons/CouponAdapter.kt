package com.example.nuka2024_try.ui.coupons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nuka2024_try.R

// クーポンデータクラス
data class Coupon(val storeName: String, val details: String, val discount: String, val expiry: String)

class CouponAdapter(private val coupons: MutableList<Coupon>) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    class CouponViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeName: TextView = itemView.findViewById(R.id.storeName)
        val couponDetails: TextView = itemView.findViewById(R.id.couponDetails)
        val couponExpiry: TextView = itemView.findViewById(R.id.couponExpiry)
        val useButton: Button = itemView.findViewById(R.id.useCouponButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coupon_item, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]

        // クーポン情報をバインド
        holder.storeName.text = coupon.storeName
        holder.couponDetails.text = coupon.details
        holder.couponExpiry.text = "有効期限: ${coupon.expiry}"

        // "使用済み"状態をリセット
        holder.useButton.text = "使用する"
        holder.useButton.isEnabled = true

        holder.useButton.setOnClickListener {
            // 使用済み処理
            holder.useButton.text = "使用済み"
            holder.useButton.isEnabled = false
        }
    }

    override fun getItemCount(): Int {
        return coupons.size
    }

    // クーポンリストを更新
    fun updateCoupons(newCoupons: List<Coupon>) {
        coupons.clear()
        coupons.addAll(newCoupons)
        notifyDataSetChanged()
    }
}
