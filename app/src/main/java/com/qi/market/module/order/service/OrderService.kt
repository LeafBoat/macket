package com.qi.market.module.order.service

import com.qi.market.module.shoppingcart.bean.OrderBean
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Qi on 2017/11/27.
 */
interface OrderService{
    @POST("order/submitorder")
    fun submitOrder(@Body orders:List<OrderBean>):Observable<ResponseBody>
}