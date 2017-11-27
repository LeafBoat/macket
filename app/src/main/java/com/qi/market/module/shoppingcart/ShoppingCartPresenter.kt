package com.qi.market.module.shoppingcart

import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.shoppingcart.activity.ShoppingCartActivity
import com.qi.market.module.shoppingcart.bean.OrderBean
import com.qi.market.module.shoppingcart.db.dao.ShoppingCartDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Qi on 2017/11/26.
 */
class ShoppingCartPresenter(activity: ShoppingCartActivity) {
    val mData = ArrayList<MerchandiseBean>()
    private val mActivity = activity
    private var shoppingCartDao: ShoppingCartDao = ShoppingCartDao(activity)
    private val factory = Executors.defaultThreadFactory()
    fun queryAll(onQueryFinished: (data: List<MerchandiseBean>) -> Unit) {
        factory.newThread {
            mData.clear()
            mData.addAll(shoppingCartDao.queryAll())
            mActivity.run {
                onQueryFinished.invoke(mData)
            }
        }.start()
    }
}