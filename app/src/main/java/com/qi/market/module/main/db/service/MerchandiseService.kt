package com.qi.market.module.main.db.service

import com.qi.market.common.Constant.T_SHOPPING_CART
import com.qi.market.common.db.annotation.Condition
import com.qi.market.common.db.annotation.QUERY
import com.qi.market.module.main.bean.MerchandiseBean
import rx.Observable

/**
 * Created by Qi on 2017/11/30.
 */
interface MerchandiseService {
    @QUERY(T_SHOPPING_CART)
    fun query(@Condition("id") id: Long?): Observable<List<MerchandiseBean>>

    @QUERY(T_SHOPPING_CART)
    fun query(): Observable<List<MerchandiseBean>>
}