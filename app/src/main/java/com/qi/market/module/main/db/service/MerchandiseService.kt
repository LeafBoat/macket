package com.qi.market.module.main.db.service

import com.qi.market.common.Constant.SHOPPINGCART_TABNAME
import com.qi.market.common.db.annotation.Condition
import com.qi.market.common.db.annotation.QUERY
import com.qi.market.module.main.bean.MerchandiseBean
import rx.Observable

/**
 * Created by Qi on 2017/11/30.
 */
interface MerchandiseService {
    @QUERY(SHOPPINGCART_TABNAME)
    fun query(@Condition("id") id: Long?): Observable<List<MerchandiseBean>>

    @QUERY(SHOPPINGCART_TABNAME)
    fun query(): Observable<List<MerchandiseBean>>
}