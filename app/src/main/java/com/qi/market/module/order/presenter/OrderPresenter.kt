package com.qi.market.module.order.presenter

import com.qi.market.common.SharePreferenceHelper
import com.qi.market.module.order.activity.OrderActivity
import com.qi.market.module.order.service.OrderService
import com.qi.market.module.shoppingcart.bean.OrderBean
import com.qi.market.module.shoppingcart.bean.OrderDetailBean
import com.qi.market.module.shoppingcart.db.dao.ShoppingCartDao
import com.qi.market.network.RetrofitClient
import java.util.concurrent.Executors

/**
 * Created by Qi on 2017/11/27.
 */
class OrderPresenter(activity: OrderActivity) {
    val activity = activity
    private val shoppingCartDao = ShoppingCartDao(activity)
    private val executorService = Executors.newCachedThreadPool()!!
    var num = 0
    var picUrls: Array<String?>? = null

    fun initData() {
        picUrls = arrayOfNulls<String>(activity.data.size)
        for (i in 0 until activity.data.size) {
            num += activity.data[i].num
            picUrls!![i] = activity.data[i].picpath
        }
    }

    fun submitOrder() {
        var orderList = ArrayList<OrderBean>()
        var orderBean = OrderBean()
        orderBean.userid = SharePreferenceHelper.getUserBean(activity)?.id
        var orderDetailList = ArrayList<OrderDetailBean>()
        for (bean in activity.data) {
            var orderDetailBean = OrderDetailBean()
            orderDetailBean.productid = bean.id
            orderDetailBean.num = bean.num.toLong()
            orderDetailList.add(orderDetailBean)
        }
        orderBean.productidlist = orderDetailList
        orderList.add(orderBean)
        var observable = RetrofitClient.create(OrderService::class.java).submitOrder(orderList)
        RetrofitClient.create(observable)
                .subscribe({
                    executorService.execute {
                        for (bean in activity.data) {
                            shoppingCartDao.delete(bean.id!!)
                        }
                        activity.finish()
                    }
                }, {}, {})
    }
}