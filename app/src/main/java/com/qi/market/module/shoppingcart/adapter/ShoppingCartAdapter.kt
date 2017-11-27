package com.qi.market.module.shoppingcart.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import com.qi.market.R
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.network.glide.GlideApp

/**
 * Author:liuqi
 * Date:2017/11/27 16:36
 * Detail:
 */
class ShoppingCartAdapter(data: List<MerchandiseBean>) : RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {
    private var mData: List<MerchandiseBean>? = null
    var onItemCheckedChangeListener: ((isChecked: Boolean, position: Int) -> Unit)? = null

    init {
        mData = data
    }

    override fun getItemCount(): Int {
        if (mData == null)
            return 0
        return mData!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var bean = mData!![position]
        holder?.radioButton?.isChecked = false
        holder?.radioButton?.setOnCheckedChangeListener { _, isChecked ->
            bean.isChecked = isChecked
            onItemCheckedChangeListener?.invoke(isChecked, position)
        }
        holder?.invalidView?.visibility = View.GONE
        GlideApp.with(holder?.imageView)
                .load(bean.picpath)
                .centerCrop()
                .placeholder(R.drawable.merchandise_default)
                .into(holder?.imageView)
        holder?.radioButton?.isChecked = bean.isChecked
        holder?.invalidView?.visibility = if (bean.isInvalid) View.VISIBLE else View.GONE
        holder?.brandView?.text = bean.title
        holder?.priceView?.text = bean.price.toString()
        holder?.sellnumsView?.text = bean.num.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_shopping_cart_adapter_child, null))

    fun notifyDataSetChanged(data: List<MerchandiseBean>?) {
        mData = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton = itemView.findViewById<RadioButton>(R.id.radioButton)
        val invalidView = itemView.findViewById<TextView>(R.id.invalidView)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val brandView = itemView.findViewById<TextView>(R.id.brandView)
        val merchandiseNameView = itemView.findViewById<TextView>(R.id.merchandiseNameView)
        val sellnumsView = itemView.findViewById<TextView>(R.id.sellnumsView)
        val unitView = itemView.findViewById<TextView>(R.id.unitView)
        val priceView = itemView.findViewById<TextView>(R.id.priceView)
        val addView = itemView.findViewById<TextView>(R.id.addView)
    }

}