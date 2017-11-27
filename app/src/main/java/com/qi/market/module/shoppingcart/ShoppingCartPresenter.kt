package com.qi.market.module.shoppingcart

import com.qi.market.module.shoppingcart.activity.ShoppingCartActivity
import com.qi.market.module.shoppingcart.bean.OrderBean
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Qi on 2017/11/26.
 */
class ShoppingCartPresenter(activity: ShoppingCartActivity) {
    val mData = ArrayList<OrderBean>()
    var threadPool: ExecutorService = Executors.newCachedThreadPool()

}