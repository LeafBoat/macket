package com.qi.market.module.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.module.detail.adapter.MerchandiseDetailPagerAdapter
import com.qi.market.module.main.bean.MerchandiseBean
import kotlinx.android.synthetic.main.activity_merchandise_detail.*

/**
 * Created by Qi on 2017/11/11.
 */
class MerchandiseDetailActivity : BaseActivity() {
    private lateinit var mMerchandiseBean: MerchandiseBean
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchandise_detail)
        initData()
        var arrayList = ArrayList<String>()
        mMerchandiseBean.picpath?.let { arrayList.add(it) }
        viewpager.adapter = MerchandiseDetailPagerAdapter(arrayList)
        skuView.text = mMerchandiseBean.title
        sellnumsView.text = "月售" + mMerchandiseBean.sellnums
        priceView.text = "￥${mMerchandiseBean.price}"
        detailView.text = mMerchandiseBean.description
    }

    private fun initData() {
        mMerchandiseBean = Gson().fromJson<MerchandiseBean>(intent.getStringExtra(MERCHANDISE), MerchandiseBean::class.java)
    }


    companion object {
        private val MERCHANDISE = "MERCHANDISE"
        fun startActivity(context: Context, merchandiseBean: MerchandiseBean) {
            context.startActivity(Intent(context, MerchandiseDetailActivity::class.java).putExtra(MERCHANDISE, Gson().toJson(merchandiseBean)))
        }
    }

}