package com.qi.market.module.search

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.module.main.adapter.MerchandiseAdapter
import com.qi.market.module.main.bean.MerchandiseBean
import kotlinx.android.synthetic.main.activity_search.*

/**
 * 搜索页面
 * Created by Qi on 2017/11/13.
 */
class SearchActivity : BaseActivity() {
    lateinit var adapter: MerchandiseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        var presenter = SearchPresenter(this)
        searchView.setOnClickListener {
            presenter.search(editText.text.toString())
        }
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter = MerchandiseAdapter(null)
        recyclerView.adapter = adapter
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun dismissProgressBar() {
        progressBar.visibility = View.GONE
    }

    fun showList() {
        dismissProgressBar()
        recyclerView.visibility = View.VISIBLE
    }

    fun notifyDataSetChanged(data: List<MerchandiseBean>?) {
        showList()
        adapter.notifyDataSetChanged(data)
    }
}