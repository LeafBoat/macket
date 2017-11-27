package com.qi.market.module.main.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.qi.market.R
import com.qi.market.module.main.bean.MerchandiseCategoryBean

/**
 * 商品类别列表适配器
 * Created by Qi on 2017/11/10.
 */
class MerchandiseMenuAdapter(data: List<MerchandiseCategoryBean>?) : RecyclerView.Adapter<ViewHolder>() {
    private var mData = data
    var mOnItemClickListener: ((merchandiseCategoryBean: MerchandiseCategoryBean, position: Int) -> Unit)? = null
    override fun getItemCount() = if (mData == null || mData!!.isEmpty()) 0 else mData!!.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val context = parent?.context
        val textView = TextView(context)
        val width = (context?.resources?.getDimension(R.dimen.main_merchandise_categories_item_width))?.toInt()
        val height = (context?.resources?.getDimension(R.dimen.main_merchandise_categories_item_height))?.toInt()
        textView.gravity = Gravity.CENTER
        textView.layoutParams = RecyclerView.LayoutParams(width!!, height!!)
        textView.setBackgroundColor(Color.LTGRAY)
        return object : ViewHolder(textView) {}
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var textView = holder?.itemView as TextView
        textView.text = mData!![position].description
        textView.setOnClickListener {
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!(mData!![position], position)
            }
        }
    }

    fun notifyDataSetChanged(data: List<MerchandiseCategoryBean>?) {
        mData = data
        notifyDataSetChanged()
    }


}
