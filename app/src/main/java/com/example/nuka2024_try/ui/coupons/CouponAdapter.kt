package com.example.nuka2024_try.ui.coupons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nuka2024_try.R

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
        holder.storeName.text = coupon.storeName
        holder.couponDetails.text = coupon.details
        holder.couponExpiry.text = coupon.expiry

        holder.useButton.setOnClickListener {
            holder.useButton.text = "使用済み"
            holder.useButton.isEnabled = false
        }
    }

    override fun getItemCount(): Int {
        return coupons.size
    }

    fun updateCoupons(newCoupons: List<Coupon>) {
        coupons.clear()
        coupons.addAll(newCoupons)
        notifyDataSetChanged()
    }
}
