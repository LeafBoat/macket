package com.qi.market.module.main.model

import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.main.bean.MerchandiseCategoryBean
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Qi on 2017/11/21.
 */
interface MainService {
    /**
     * 获取商品类别
     */
    @POST("productTypes/listproducttypes")
    fun getMerchandiseCategory(): Observable<List<MerchandiseCategoryBean>>

    @POST("product/listproduct")
    fun getMerchandise(@Body merchandiseCategoryBean: MerchandiseCategoryBean): Observable<List<MerchandiseBean>>
}