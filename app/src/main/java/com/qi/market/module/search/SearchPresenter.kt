package com.qi.market.module.search

import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.network.RetrofitClient

/**
 * Created by Qi on 2017/11/19.
 */
class SearchPresenter(activity: SearchActivity) {
    private val mActivity = activity
    fun search(key: String) {
        mActivity.showProgressBar()
        val merchandiseBean = MerchandiseBean()
        merchandiseBean.title = key
        var observable = RetrofitClient.create(SearchService::class.java).search(merchandiseBean)
        RetrofitClient.create(observable).subscribe({
            if (it != null && !it.isEmpty()) {
                mActivity.notifyDataSetChanged(it)
            }
        }, {
            it.printStackTrace()
        }, {
            mActivity.dismissProgressBar()
        })
    }
}