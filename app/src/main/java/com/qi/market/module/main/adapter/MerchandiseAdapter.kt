package com.qi.market.module.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import com.qi.market.R
import com.qi.market.module.detail.MerchandiseDetailActivity
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.network.glide.GlideApp

/**
 * 首页商品列表适配器
 * Created by Qi on 2017/11/10.
 */
class MerchandiseAdapter(data: List<MerchandiseBean>?) : RecyclerView.Adapter<MerchandiseAdapter.MerchandiseViewHolder>() {

    var mData = data
    var onMerchandiseNumChangeListener: ((merchandiseBean: MerchandiseBean, position: Int) -> Unit)? = null
    override fun getItemCount() = if (mData == null || mData!!.isEmpty()) 0 else mData!!.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MerchandiseViewHolder {
        var context = parent?.context
        var view = LayoutInflater.from(context).inflate(R.layout.item_merchandise, null)
        view.layoutParams = AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return MerchandiseViewHolder(view)
    }

    override fun onBindViewHolder(holder: MerchandiseViewHolder, position: Int) {
        var merchandiseBean = mData!![position]
        //初始化
        holder.subtractView.visibility = View.GONE
        holder.numView.visibility = View.GONE
        GlideApp.with(holder?.context)
                .load(merchandiseBean.picpath)
                .centerCrop()
                .placeholder(R.drawable.merchandise_default)
                .into(holder!!.imageView)
        holder.brandTV.text = merchandiseBean.title
        holder.priceTV.text = "￥" + merchandiseBean.price
        holder.sellnumsView.text = "月售" + merchandiseBean.sellnums
        holder.itemView.setOnClickListener {
            MerchandiseDetailActivity.startActivity(it.context, merchandiseBean)
        }
        if (merchandiseBean.num > 0) {
            holder.subtractView.visibility = View.VISIBLE
            holder.numView.visibility = View.VISIBLE
        }
        holder.subtractView.setOnClickListener {
            merchandiseBean.num--
            notifyItemChanged(position)
            if (onMerchandiseNumChangeListener != null)
                onMerchandiseNumChangeListener!!.invoke(merchandiseBean, position)
        }
        holder.numView.text = merchandiseBean.num.toString()
        holder.addTV.setOnClickListener {
            merchandiseBean.num++
            notifyItemChanged(position)
            if (onMerchandiseNumChangeListener != null)
                onMerchandiseNumChangeListener!!.invoke(merchandiseBean, position)
        }
    }

    fun notifyDataSetChanged(data: List<MerchandiseBean>?) {
        mData = data
        notifyDataSetChanged()
    }

    class MerchandiseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context!!
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)!!
        val brandTV = itemView.findViewById<TextView>(R.id.brandView)!!
        val merchandiseNameTV = itemView.findViewById<TextView>(R.id.merchandiseNameView)!!
        val volumeTV = itemView.findViewById<TextView>(R.id.unitView)!!
        val sellnumsView = itemView.findViewById<TextView>(R.id.sellnumsView)!!
        val praiseTV = itemView.findViewById<TextView>(R.id.praiseView)!!
        val priceTV = itemView.findViewById<TextView>(R.id.priceView)!!
        val subtractView = itemView.findViewById<TextView>(R.id.subtractView)!!
        val numView = itemView.findViewById<TextView>(R.id.numView)!!
        val addTV = itemView.findViewById<TextView>(R.id.addView)!!
    }
}

