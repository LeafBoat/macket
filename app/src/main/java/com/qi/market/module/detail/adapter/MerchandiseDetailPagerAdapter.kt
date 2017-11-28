package com.qi.market.module.detail.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.qi.market.R
import com.qi.market.network.glide.GlideApp

/**
 * Created by Qi on 2017/11/23.
 */
class MerchandiseDetailPagerAdapter(urls: ArrayList<String>?) : PagerAdapter() {
    private var mData = urls

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        var imageView = ImageView(container!!.context)
        GlideApp.with(container!!.context)
                .load(mData!![position])
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(imageView)
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container!!.removeView(`object` as View?)
    }


    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        if (mData == null || mData!!.isEmpty()) {
            return 0
        }
        return mData!!.size
    }

    fun notifyDataSetChanged(urls: ArrayList<String>?) {
        mData = urls
        super.notifyDataSetChanged()
    }

}