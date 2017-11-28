package com.qi.market.network.retrofit

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import com.qi.market.R
import com.qi.market.base.BaseBean
import com.qi.market.common.Constant
import com.qi.market.network.retrofit.widget.DataContainerView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
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

        private fun getDialog(context: Context): Dialog {
            var dialog = Dialog(context)
            dialog.addContentView(LayoutInflater.from(context).inflate(R.layout.layout_progress, null),
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            dialog.window.setGravity(Gravity.CENTER)
            var layoutParams = dialog.window.attributes
            layoutParams.gravity = Gravity.CENTER
            return dialog
        }

        fun <S> create(service: Class<S>) = retrofit.create(service)!!

        fun <T> create(observable: Observable<T>): Observable<T> {
            return observable.compose { observable: Observable<T> ->
                return@compose observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }

        /**
         * @param parent 展示数据的View,如果加载数据失败，可以用该View添加失败
         */
        fun <T> create(view: DataContainerView?, observable: Observable<BaseBean<T>>, onSucceed: (data: T) -> Unit, onFail: (msg: String) -> Unit) {
            view?.showProgressBar()
            observable.compose { observable: Observable<BaseBean<T>> ->
                return@compose observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }.subscribe({
                if (it.code == 200) {
                    view?.showDataView()
                    onSucceed!!(it.data)
                } else {
                    view?.showErrorView()
                    onFail(it.msg)
                }
            }, {
                view?.showErrorView()
                it.printStackTrace()
            })
        }

        /**
         * @param parent 展示数据的View,如果加载数据失败，可以用该View添加失败
         */
        fun <T> create(observable: Observable<BaseBean<T>>, onSucceed: (data: T) -> Unit, onFail: (msg: String) -> Unit) {
            create(null, observable, onSucceed, onFail)
        }
    }

}