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
    private val onUseButtonClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    class CouponViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val companyName: TextView = view.findViewById(R.id.textCompanyName)
        val title: TextView = view.findViewById(R.id.textCouponTitle)
        val discount: TextView = view.findViewById(R.id.textCouponDiscount)
        val expiration: TextView = view.findViewById(R.id.textCouponExpiration)
        val useButton: Button = view.findViewById(R.id.buttonUseCoupon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon, parent, false)
        return CouponViewHolder(view)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = coupons[position]
        holder.companyName.text = coupon.companyName
        holder.title.text = coupon.title
        holder.discount.text = coupon.discount
        holder.expiration.text = coupon.expiration

        if (coupon.isUsed) {
            holder.useButton.text = "使用済み"
            holder.useButton.isEnabled = false
        } else {
            holder.useButton.text = "クーポンを使用する"
            holder.useButton.isEnabled = true
            holder.useButton.setOnClickListener {
                onUseButtonClicked(position)
            }
        }
    }

    override fun getItemCount() = coupons.size
}
