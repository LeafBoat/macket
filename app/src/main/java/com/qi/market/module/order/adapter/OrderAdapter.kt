package com.qi.market.module.order.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.qi.market.R
import com.qi.market.network.glide.GlideApp

/**
 * Created by Qi on 2017/11/27.
 */
class OrderAdapter(urls: Array<String?>?) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    var urls = urls
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        GlideApp.with(holder?.itemView?.context)
                .load(urls!![position])
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(holder!!.itemView as ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val context = parent?.context
        val width = (context?.resources?.getDimension(R.dimen.img_width)!! + 0.5f).toInt()
        val height = (context?.resources?.getDimension(R.dimen.img_height)!! + 0.5f).toInt()
        val imageView = ImageView(context)
        imageView.layoutParams = RecyclerView.LayoutParams(width, height)
        return ViewHolder(imageView)
    }

    override fun getItemCount(): Int {
        return if (urls == null)
            0
        else
            urls!!.size
    }

    fun notifyDataSetChanged(urls: Array<String?>?){
        this.urls = urls
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}