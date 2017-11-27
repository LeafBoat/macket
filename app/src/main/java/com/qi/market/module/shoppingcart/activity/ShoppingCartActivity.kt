package com.qi.market.module.shoppingcart.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.order.activity.OrderActivity
import com.qi.market.module.shoppingcart.ShoppingCartPresenter
import com.qi.market.module.shoppingcart.adapter.ShoppingCartAdapter
import kotlinx.android.synthetic.main.activity_shopping_cart.*

/**
 * 购物车页面
 * Created by Qi on 2017/11/24.
 */
class ShoppingCartActivity : BaseActivity() {
    private var deletable = false
    lateinit var adapter: ShoppingCartAdapter
    private lateinit var presenter: ShoppingCartPresenter
    private var totalMoney: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)
        presenter = ShoppingCartPresenter(this)
        adapter = ShoppingCartAdapter(presenter.mData)
        adapter.onItemCheckedChangeListener = { _, _ ->
            //当商品选中状态改变总金额跟着改变
            calculateTotalMoney()
        }
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        checkAllView.setOnCheckedChangeListener { _, isChecked ->
            presenter.mData
                    .takeWhile { !it.isInvalid }
                    .forEach { it.isChecked = isChecked }
            adapter.notifyDataSetChanged(presenter.mData)
            calculateTotalMoney()
        }
        totalMoneyView.text = String.format(getString(R.string.total_money), totalMoney)
        settlementView.setOnClickListener {
            var checkedData: List<MerchandiseBean> = presenter.mData.filter { it.isChecked && !it.isInvalid }
            if (checkedData == null || checkedData.isEmpty()) {
                Toast.makeText(it.context, "请选择商品", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            OrderActivity.startActivity(it.context, totalMoney,checkedData)
        }
        removeView.setOnClickListener {
            deletable = !deletable
            changeAction()
        }
        presenter.queryAll {
            adapter.notifyDataSetChanged(it)
        }
    }

    private fun calculateTotalMoney() {
        totalMoney = 0.0
        presenter.mData.filter { !it.isInvalid && it.isChecked }.forEach { totalMoney += it.price * it.num }
        totalMoneyView.text = String.format(getString(R.string.total_money), totalMoney)
    }

    private fun changeAction() = if (deletable) {
        actionSettlementView.visibility = View.GONE
    } else {
        actionSettlementView.visibility = View.VISIBLE
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(
                    Intent(context, ShoppingCartActivity::class.java)
            )
        }
    }
}

