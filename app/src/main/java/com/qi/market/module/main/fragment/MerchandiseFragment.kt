package com.qi.market.module.main.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qi.market.R
import com.qi.market.base.BaseFragment
import com.qi.market.module.main.activity.MainActivity
import com.qi.market.module.main.adapter.MerchandiseAdapter
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.fragment_merchandise.*

/**
 * 商品展示Fragment
 * Created by Qi on 2017/11/10.
 */
class MerchandiseFragment : BaseFragment() {

    private var mMerchandiseAdapter: MerchandiseAdapter? = null

    private lateinit var mMainActivity: MainActivity

    private lateinit var presenter: MainPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = inflater?.inflate(R.layout.fragment_merchandise, null)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mMainActivity = activity as MainActivity
        presenter = mMainActivity.presenter
        mMerchandiseAdapter = MerchandiseAdapter(null)
        mMerchandiseAdapter!!.onMerchandiseNumChangeListener = { merchandiseBean, _ ->
            if (merchandiseBean.num < 1) {
                mMainActivity.presenter.delete(merchandiseBean)
            } else {
                mMainActivity.presenter.updateShoppingCart(merchandiseBean)
            }
        }
        mMerchandiseAdapter = mMerchandiseAdapter
        merchandiseList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        merchandiseList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        merchandiseList.adapter = mMerchandiseAdapter
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter.merchandiseList()
    }


    fun refresh(data: List<MerchandiseBean>?, oldId: Long?) {
        presenter.refreshMerchandiseNum()
        mMainActivity.presenter.changeMerchandiseCheckedNum(data, {
            if (mMainActivity.mMerchandiseCategoryId != oldId)
                return@changeMerchandiseCheckedNum
            mMerchandiseAdapter!!.notifyDataSetChanged(data)
        })
    }


    /**
     * 展示错误页面
     */
    fun showErrorView() {
        errorView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    fun showProgressBar() {
        errorView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    fun showContentView() {
        errorView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }
}