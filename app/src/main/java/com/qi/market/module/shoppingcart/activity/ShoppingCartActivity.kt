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
import com.qi.market.module.shoppingcart.db.dao.ShoppingCartDao
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import java.util.ArrayList

/**
 * 购物车页面
 * Created by Qi on 2017/11/24.
 */
class ShoppingCartActivity : BaseActivity() {
    private var deletable = false
    lateinit var adapter: ShoppingCartAdapter
    private lateinit var presenter: ShoppingCartPresenter
    private var totalMoney: Double = 0.0
    /**
     * 是否可以更新集合中所有的数据的选中状态
     */
    private var isUpdateAllDataSelectedState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)
        presenter = ShoppingCartPresenter(this)
        adapter = ShoppingCartAdapter(presenter.mData)
        adapter.onItemCheckedChangeListener = { _, _ ->
            //当商品选中状态改变总金额跟着改变
            val validNum = presenter.mData.count { !it.isInvalid }
            val checkedValidNum = presenter.mData.count { !it.isInvalid && it.isChecked }
            isUpdateAllDataSelectedState = false
            checkAllView.isChecked = validNum == checkedValidNum
            isUpdateAllDataSelectedState = true
            calculateTotalMoney()
        }
        adapter.onNumChangedListener = { merchandiseBean, _ ->
            val validNum = presenter.mData.count { !it.isInvalid }
            val checkedValidNum = presenter.mData.count { !it.isInvalid && it.isChecked }
            isUpdateAllDataSelectedState = false
            checkAllView.isChecked = validNum == checkedValidNum
            isUpdateAllDataSelectedState = true
            calculateTotalMoney()
            if (merchandiseBean.num < 1) {
                presenter.delete(merchandiseBean)
            } else {
                presenter.update(merchandiseBean)
            }
        }
        editView.setOnClickListener {
            deletable = !deletable
            changeAction()
        }
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        checkAllView.setOnCheckedChangeListener { _, isChecked ->
            if (isUpdateAllDataSelectedState) {
                presenter.mData
                        .takeWhile { !it.isInvalid }
                        .forEach { it.isChecked = isChecked }
            }
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
            OrderActivity.startActivity(it.context, totalMoney, checkedData)
        }
        removeView.setOnClickListener {
            val checkedData = ArrayList<MerchandiseBean>()
            for (bean in presenter.mData) {
                if (bean.isChecked) {
                    checkedData.add(bean)
                    presenter.mData.remove(bean)
                }
            }
            presenter.delete(checkedData)
            adapter.notifyDataSetChanged(presenter.mData)
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

    /**
     * 改变行为，修改最下方的条目内容
     */
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

