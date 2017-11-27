package com.qi.market.module.shoppingcart.adapter

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qi.market.R
import com.qi.market.module.shoppingcart.bean.OrderBean

/**
 * 购物车页面
 * Created by Qi on 2017/11/26.
 */
class ShoppingCartAdapter(data: List<OrderBean>) : RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {
    var mData = data
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var orderBean = mData[position]
        var context = holder?.itemView?.context
        holder?.orderNumView?.text = "订单$position"
        if (holder?.recyclerView?.adapter == null) {
            holder?.recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            holder?.recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            holder?.recyclerView?.adapter = ShoppingCartChildAdapter(orderBean.productidlist)
        } else {
            var adapter = holder.recyclerView.adapter as ShoppingCartChildAdapter
            adapter.notifyDataSetChanged(orderBean.productidlist)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
            = ViewHolder(View.inflate(parent?.context, R.layout.item_shopping_cart_adapter, null))

    override fun getItemCount(): Int {
        return if (mData == null) 0
        else mData.size
    }

    fun notifyDataSetChanged(data: List<OrderBean>) {
        mData = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderNumView = itemView.findViewById<TextView>(R.id.orderNumView)!!
        val recyclerView = itemView.findViewById<RecyclerView>(R.id.recyclerView)!!
    }

}

