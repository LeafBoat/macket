package com.qi.market.network

import okhttp3.ResponseBody
import retrofit2.http.*
import rx.Observable


/**
 * Created by Qi on 2017/11/19.
 */
interface RxService {
    @GET("{url}")
    fun executeGet(@Path("url") url: String, @QueryMap maps: Map<String, String>): Observable<ResponseBody>

    @POST("{url}")
    fun executePost(@Path("url") url: String, @Body body: Any): Observable<ResponseBody>
}