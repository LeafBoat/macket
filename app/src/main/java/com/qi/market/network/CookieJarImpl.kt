package com.qi.market.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Created by Qi on 2017/11/16.
 */
class CookieJarImpl : CookieJar {
    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {

    }

    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
        return ArrayList()
    }
}