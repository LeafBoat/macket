package com.qi.market.module.order.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.common.SharePreferenceHelper
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.order.adapter.OrderAdapter
import com.qi.market.module.order.presenter.OrderPresenter
import com.qi.market.module.shoppingcart.bean.OrderBean
import com.qi.market.module.shoppingcart.bean.OrderDetailBean
import kotlinx.android.synthetic.main.activity_order.*

/**
 * Created by Qi on 2017/11/27.
 */
class OrderActivity : BaseActivity() {

    var presenter = OrderPresenter(this)
    lateinit var data: List<MerchandiseBean>
    var adapter: OrderAdapter = OrderAdapter(null)

    private var totalMoney: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        data = Gson().fromJson(intent.getStringExtra(MERCHANDISE_LIST), object : TypeToken<List<MerchandiseBean>>() {}.type)
        totalMoney = intent.getDoubleExtra(MONEY, 0.0)
        presenter.initData()
        numView.text = String.format(getString(R.string.merchandiseNum), presenter.num)
        adapter.notifyDataSetChanged(presenter.picUrls)
        actuallyPaidView.text = String.format(getString(R.string.actuallyPaid), totalMoney)
        submitOrderView.setOnClickListener {
            presenter.submitOrder()
        }
    }

    companion object {
        val MERCHANDISE_LIST = "MERCHANDISE_LIST"
        val MONEY = "MONEY"
        fun startActivity(context: Context, totalMoney: Double, checkedMerchandises: List<MerchandiseBean>){
            context.startActivity(
                    Intent(context,OrderActivity::class.java)
                            .putExtra(MONEY,totalMoney)
                            .putExtra(MERCHANDISE_LIST,Gson().toJson(checkedMerchandises))
            )
        }
    }
}