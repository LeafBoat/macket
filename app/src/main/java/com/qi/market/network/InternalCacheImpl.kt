package com.qi.market.network

import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.cache.CacheRequest
import okhttp3.internal.cache.CacheStrategy
import okhttp3.internal.cache.InternalCache

/**
 * Created by Qi on 2017/11/16.
 */
class InternalCacheImpl:InternalCache {
    override fun update(cached: Response?, network: Response?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun put(response: Response?): CacheRequest {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(request: Request?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(request: Request?): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackConditionalCacheHit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun trackResponse(cacheStrategy: CacheStrategy?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}