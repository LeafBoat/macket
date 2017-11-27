package com.qi.market.module.shoppingcart.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.qi.market.R
import com.qi.market.module.shoppingcart.bean.OrderDetailBean

/**
 * 购物车二级适配器
 * Created by Qi on 2017/11/26.
 */
class ShoppingCartChildAdapter(data: List<OrderDetailBean>?) : RecyclerView.Adapter<ShoppingCartChildAdapter.ViewHolder>() {
    private var mData: List<OrderDetailBean>? = null
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
        holder?.checkbox?.isChecked = false
        holder?.checkbox?.setOnCheckedChangeListener { _, isChecked ->
            bean.isChecked = isChecked
            onItemCheckedChangeListener?.invoke(isChecked, position)
        }
        holder?.invalidView?.visibility = View.GONE
        /*GlideApp.with(holder?.imageView)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.merchandise_default)
                .into(holder?.imageView)*/
        holder?.checkbox?.isChecked = bean.isChecked
        holder?.invalidView?.visibility = if (bean.isInvalid) View.VISIBLE else View.GONE
        holder?.brandView?.text = bean.producttile
        holder?.priceView?.text = bean.productprice.toString()
        holder?.sellnumsView?.text = bean.num.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_shopping_cart_adapter_child, null))

    fun notifyDataSetChanged(data: List<OrderDetailBean>?) {
        mData = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)
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

