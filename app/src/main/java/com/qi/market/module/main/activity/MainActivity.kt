package com.qi.market.module.main.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.common.db.main.SQLiteDao
import com.qi.market.module.main.adapter.MerchandiseMenuAdapter
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.main.bean.MerchandiseCategoryBean
import com.qi.market.module.main.db.service.MerchandiseService
import com.qi.market.module.main.fragment.MerchandiseFragment
import com.qi.market.module.main.presenter.MainPresenter
import com.qi.market.module.search.SearchActivity
import com.qi.market.module.shoppingcart.activity.ShoppingCartActivity
import com.qi.market.module.shoppingcart.db.ShoppingCartSQLiteOpenHelper
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 主页，展示商品列表
 */
class MainActivity : BaseActivity() {
    lateinit var presenter: MainPresenter
    private var mMenuAdapter: MerchandiseMenuAdapter? = null
    var mMerchandiseCategoryId: Long = -1
    val dao = SQLiteDao.Builder().setSQLiteOpenHelper(ShoppingCartSQLiteOpenHelper(this)).build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showProgressBar()
        presenter = MainPresenter(this)
        searchView.setOnClickListener {
            startActivity(Intent(it.context, SearchActivity::class.java))
        }
        shoppingCartActionView.setOnClickListener {
            ShoppingCartActivity.startActivity(it.context)
        }
        setMenu()
    }

    /**
     * 设置商品列表
     */
    fun setList() {
        val fragment = MerchandiseFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment, MERCHANDISE_FRAGMENT).commit()
    }

    /**
     * 刷新商品列表
     */
    fun refreshList(data: List<MerchandiseBean>, oldId: Long?) {
        if (mMerchandiseCategoryId != oldId)
            return
        var fragment = supportFragmentManager.findFragmentByTag(MERCHANDISE_FRAGMENT) as MerchandiseFragment
        fragment.refresh(data, oldId)
    }

    /**
     * 设置商品类别菜单
     */
    private fun setMenu() {
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mMenuAdapter = MerchandiseMenuAdapter(null)
        mMenuAdapter!!.mOnItemClickListener = { merchandiseCategoryBean, _ ->
            mMerchandiseCategoryId = merchandiseCategoryBean.id!!
            presenter.currentCategory = merchandiseCategoryBean
            presenter.merchandiseList()
        }
        list.adapter = mMenuAdapter
        presenter.merchandiseCategory()
    }

    /**
     * 刷新菜单
     */
    fun refreshMenu(meun: List<MerchandiseCategoryBean>) {
        mMenuAdapter!!.notifyDataSetChanged(meun)
    }

    /**
     * 整体页面加载
     */
    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        contentView.visibility = View.GONE
        errorView.visibility = View.GONE
    }

    fun showErrorView() {
        errorView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        contentView.visibility = View.GONE
    }

    fun showContentView() {
        contentView.visibility = View.VISIBLE
        errorView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    fun fragmentShowProgressbar() {
        var fragment = supportFragmentManager.findFragmentByTag(MERCHANDISE_FRAGMENT) as MerchandiseFragment
        fragment.showProgressBar()
    }

    fun fragmentShowContentView() {
        var fragment = supportFragmentManager.findFragmentByTag(MERCHANDISE_FRAGMENT) as MerchandiseFragment
        fragment.showContentView()
    }

    fun fragmentShowErrorView() {
        var fragment = supportFragmentManager.findFragmentByTag(MERCHANDISE_FRAGMENT) as MerchandiseFragment
        fragment.showErrorView()
    }

    companion object {
        val MERCHANDISE_FRAGMENT = "MERCHANDISE_FRAGMENT"
    }

    fun updateMerchandiseTotalNum() {
        dao.create(MerchandiseService::class.java).query().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe {
            presenter.totalNum = 0
            if (!it.isEmpty()) {
                presenter.totalNum = 0
                for (bean in it) {
                    presenter.totalNum += bean.num
                }
            }
            shoppingCartActionView.text = presenter.totalNum.toString()
        }
    }
}
