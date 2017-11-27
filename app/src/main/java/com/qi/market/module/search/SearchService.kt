package com.qi.market.module.search

import com.qi.market.module.main.bean.MerchandiseBean
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Qi on 2017/11/23.
 */
interface SearchService {
    @POST("product/searchproduct")
    fun search(@Body merchandiseBean: MerchandiseBean): Observable<List<MerchandiseBean>>
}