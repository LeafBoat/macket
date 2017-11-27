package com.qi.market.module.shoppingcart.bean

/**
 * Created by Qi on 2017/11/26.
 */
class OrderBean {
    var userid: Long? = null//用户id

    var orderid: Long? = null//订单id

    var productidlist: List<OrderDetailBean>? = null//产品列表
}