package com.example.nuka2024_try.ui.coupons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nuka2024_try.R

class CouponAdapter(
    private val coupons: List<Coupon>,
    private val onUseButtonClicked: (coupon: Coupon) -> Unit
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    class CouponViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // 店舗名表示用の TextView\
        val storeName: TextView = view.findViewById(R.id.textStoreName)
        // クーポンタイトル
        val title: TextView = view.findViewById(R.id.textCouponTitle)
        val discount: TextView = view.findViewById(R.id.textCouponDiscount)
        val limit: TextView = view.findViewById(R.id.textCouponLimit)
        val useButton: Button = view.findViewById(R.id.buttonUseCoupon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]

        // 店舗名を表示
        holder.storeName.text = coupon.storeName
        // クーポンタイトルを表示
        holder.title.text = coupon.title
        holder.discount.text = coupon.discount

        // Date? を文字列に変換して表示 (Timestamp -> Date -> String)
        val formattedDate = coupon.limit?.let {
            val sdf = java.text.SimpleDateFormat("yyyy年M月d日 HH:mm:ss", java.util.Locale.getDefault())
            sdf.format(it)
        } ?: ""
        holder.limit.text = formattedDate

        if (coupon.isUsed) {
            holder.useButton.text = "使用済み"
            holder.useButton.isEnabled = false
        } else {
            holder.useButton.text = "クーポンを使用する"
            holder.useButton.isEnabled = true
            holder.useButton.setOnClickListener {
                onUseButtonClicked(coupon)
            }
        }
    }

    override fun getItemCount() = coupons.size
}

