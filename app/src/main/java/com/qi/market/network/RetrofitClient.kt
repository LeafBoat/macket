package com.qi.market.network

import com.google.gson.GsonBuilder
import com.qi.market.common.Constant
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Retrofit客户端
 * Created by Qi on 2017/11/19.
 */
class RetrofitClient private constructor() {
    companion object {
        private val httpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor())
                .build()
        private val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create()!!
        private val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constant.BASE_URL)
                .client(httpClient)
                .build()

        fun <S> create(service: Class<S>) = retrofit.create(service)!!

        fun <T> create(observable: Observable<T>): Observable<T> {
            return observable.compose { observable: Observable<T> ->
                return@compose observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

}