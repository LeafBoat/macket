package com.qi.market.module.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.module.detail.adapter.MerchandiseDetailPagerAdapter
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.shoppingcart.db.dao.ShoppingCartDao
import kotlinx.android.synthetic.main.activity_merchandise_detail.*
import java.util.concurrent.Executors

/**
 * Created by Qi on 2017/11/11.
 */
class MerchandiseDetailActivity : BaseActivity() {
    private lateinit var mMerchandiseBean: MerchandiseBean
    private lateinit var mAdapter: MerchandiseDetailPagerAdapter
    private lateinit var dao: ShoppingCartDao
    private var executorService = Executors.newCachedThreadPool()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merchandise_detail)
        initData()
        skuView.text = mMerchandiseBean.title
        sellnumsView.text = "月售" + mMerchandiseBean.sellnums
        priceView.text = "￥${mMerchandiseBean.price}"
        detailView.text = mMerchandiseBean.description
        viewpager.adapter = mAdapter
        addView.setOnClickListener {
            mMerchandiseBean.num++
            updateDataBase()
            numView.text = mMerchandiseBean.num.toString()
            addView.visibility = View.GONE
            editNumView.visibility = View.VISIBLE
        }
        decrementView.setOnClickListener {
            if (mMerchandiseBean.num < 1)
                return@setOnClickListener
            mMerchandiseBean.num--
            updateDataBase()
            numView.text = mMerchandiseBean.num.toString()
            if (mMerchandiseBean.num == 0) {
                addView.visibility = View.VISIBLE
                editNumView.visibility = View.GONE
            }
        }
        numView.text = mMerchandiseBean.num.toString()
        incrementView.setOnClickListener {
            mMerchandiseBean.num++
            updateDataBase()
            numView.text = mMerchandiseBean.num.toString()
        }
        if (mMerchandiseBean.num > 0) {
            addView.visibility = View.GONE
            editNumView.visibility = View.VISIBLE
        }
    }

    private fun initData() {
        dao = ShoppingCartDao(this)
        mMerchandiseBean = Gson().fromJson<MerchandiseBean>(intent.getStringExtra(MERCHANDISE), MerchandiseBean::class.java)
        var arrayList = ArrayList<String>()
        mMerchandiseBean.picpath?.let { arrayList.add(it) }
        mAdapter = MerchandiseDetailPagerAdapter(arrayList)
    }


    private fun updateDataBase() {
        executorService.submit {
            if (mMerchandiseBean.num < 1) {
                dao.delete(mMerchandiseBean.id!!)
            } else {
                dao.update(mMerchandiseBean)
            }
            var merchandise = dao.query(mMerchandiseBean.id!!)
            merchandise?.num
        }
    }

    companion object {
        private val MERCHANDISE = "MERCHANDISE"
        fun startActivity(context: Context, merchandiseBean: MerchandiseBean) {
            context.startActivity(Intent(context, MerchandiseDetailActivity::class.java).putExtra(MERCHANDISE, Gson().toJson(merchandiseBean)))
        }
    }

}