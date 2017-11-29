package com.qi.market.module.shoppingcart.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.qi.market.R
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.network.glide.GlideApp

/**
 * Author:liuqi
 * Date:2017/11/27 16:36
 * Detail:
 */
class ShoppingCartAdapter(data: List<MerchandiseBean>, deletable: Boolean) : RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {
    private var mData: List<MerchandiseBean>? = data
    private var deletable: Boolean = deletable
    var onItemCheckedChangeListener: ((isChecked: Boolean, position: Int) -> Unit)? = null
    var onNumChangedListener: ((merchandiseBean: MerchandiseBean, position: Int) -> Unit)? = null
    override fun getItemCount(): Int {
        if (mData == null)
            return 0
        return mData!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var bean = mData!![position]
        holder!!.checkbox.visibility = View.VISIBLE
        holder.checkbox.setOnCheckedChangeListener(null)
        holder.checkbox.isChecked = false
        holder.invalidView.visibility = View.VISIBLE
        holder.subtractView.visibility = View.GONE
        holder.subtractView.setOnLongClickListener(null)
        holder.numView.visibility = View.GONE
        holder.numView.text = "0"
        holder.addView.setOnClickListener(null)
        if (deletable) {
            holder.invalidView.visibility = View.GONE
        } else {
            if (bean.isInvalid)
                holder.checkbox.visibility = View.GONE
            else
                holder.invalidView.visibility = View.GONE
        }
        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            bean.isChecked = isChecked
            onItemCheckedChangeListener?.invoke(isChecked, position)
        }
        holder.addView.setOnClickListener {
            bean.num++
            holder.numView.text = bean.num.toString()
            if (bean.num > 1) {
                holder.numView.visibility = View.VISIBLE
                holder.subtractView.visibility = View.VISIBLE
            }
            if (onNumChangedListener != null)
                onNumChangedListener!!(bean, position)
        }
        holder.subtractView.setOnClickListener {
            if (bean.num < 1)
                return@setOnClickListener
            bean.num--
            holder.numView.text = bean.num.toString()
            if (bean.num < 1) {
                holder.numView.visibility = View.GONE
                holder.subtractView.visibility = View.GONE
            }
            if (onNumChangedListener != null)
                onNumChangedListener!!(bean, position)
        }
        GlideApp.with(holder.imageView)
                .load(bean.picpath)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(holder.imageView)
        holder.checkbox.isChecked = bean.isChecked
        holder.invalidView.visibility = if (bean.isInvalid) View.VISIBLE else View.GONE
        holder.brandView.text = bean.title
        holder.priceView.text = bean.price.toString()
        if (bean.num > 0) {
            holder.numView.text = bean.num.toString()
            holder.numView.visibility = View.VISIBLE
            holder.subtractView.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_shopping_cart_adapter, null))

    fun notifyDataSetChanged(data: List<MerchandiseBean>?, deletable: Boolean) {
        mData = data
        this.deletable = deletable
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)!!
        val invalidView = itemView.findViewById<TextView>(R.id.invalidView)!!
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)!!
        val brandView = itemView.findViewById<TextView>(R.id.brandView)!!
        val merchandiseNameView = itemView.findViewById<TextView>(R.id.merchandiseNameView)!!
        val sellnumsView = itemView.findViewById<TextView>(R.id.sellnumsView)!!
        val unitView = itemView.findViewById<TextView>(R.id.unitView)!!
        val priceView = itemView.findViewById<TextView>(R.id.priceView)!!
        val addView = itemView.findViewById<TextView>(R.id.addView)!!
        val numView = itemView.findViewById<TextView>(R.id.numView)!!
        val subtractView = itemView.findViewById<TextView>(R.id.subtractView)!!

        init {
            itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

}