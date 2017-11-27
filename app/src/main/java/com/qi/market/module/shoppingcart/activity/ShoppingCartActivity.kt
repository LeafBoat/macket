package com.qi.market.module.shoppingcart.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.module.shoppingcart.ShoppingCartPresenter
import com.qi.market.module.shoppingcart.adapter.OrderAdapter
import com.qi.market.module.shoppingcart.adapter.ShoppingCartAdapter
import com.qi.market.module.shoppingcart.db.dao.ShoppingCartDao
import kotlinx.android.synthetic.main.activity_shopping_cart.*

/**
 * 购物车页面
 * Created by Qi on 2017/11/24.
 */
class ShoppingCartActivity : BaseActivity() {
    private var deletable = false
    lateinit var adapter: ShoppingCartAdapter
    private lateinit var presenter: ShoppingCartPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)
        presenter = ShoppingCartPresenter(this)
        adapter = ShoppingCartAdapter(presenter.mData)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
        checkAllView.setOnCheckedChangeListener { _, isChecked ->
            presenter.mData
                    .takeWhile { !it.isInvalid }
                    .forEach { it.isChecked = isChecked }
        }
        adapter.notifyDataSetChanged(presenter.mData)
        removeView.setOnClickListener {
            deletable = !deletable
            changeAction()
        }
        presenter.queryAll {
            adapter.notifyDataSetChanged(it)
        }
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

