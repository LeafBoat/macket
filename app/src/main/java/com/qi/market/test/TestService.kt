package com.qi.market.test

import com.qi.market.common.db.annotation.QUERY
import com.qi.market.module.main.bean.MerchandiseBean
import rx.Observable

/**
 * Created by Qi on 2017/11/29.
 */
interface TestService {
    @QUERY("t_shoppingcart")
    fun query(): Observable<List<MerchandiseBean>>
}