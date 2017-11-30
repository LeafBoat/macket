package com.qi.market.module.shoppingcart

import com.qi.market.common.Constant
import com.qi.market.common.db.main.SQLiteDao
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.main.db.service.MerchandiseService
import com.qi.market.module.shoppingcart.activity.ShoppingCartActivity
import com.qi.market.module.shoppingcart.bean.OrderBean
import com.qi.market.module.shoppingcart.db.ShoppingCartSQLiteOpenHelper
import com.qi.market.module.shoppingcart.db.dao.ShoppingCartDao
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Qi on 2017/11/26.
 */
class ShoppingCartPresenter(activity: ShoppingCartActivity) {
    val mData = ArrayList<MerchandiseBean>()
    private val activity = activity
    private var shoppingCartDao: ShoppingCartDao = ShoppingCartDao(activity)
    private val sqLiteDao = SQLiteDao.Builder().setSQLiteOpenHelper(ShoppingCartSQLiteOpenHelper(activity)).build()
    private val factory = Executors.defaultThreadFactory()
    fun queryAll(onQueryFinished: (data: List<MerchandiseBean>) -> Unit) {
        mData.clear()
        sqLiteDao.create(MerchandiseService::class.java).query().observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    for (bean in it){
                        bean.picpath = Constant.BASE_URL+bean.picpath
                    }
                    mData.addAll(it)
                    onQueryFinished.invoke(mData)
                }, {}, {})
    }

    fun delete(merchandiseBean: MerchandiseBean) {
        factory.newThread {
            shoppingCartDao.delete(merchandiseBean.id!!)
        }.start()
    }

    fun delete(list: List<MerchandiseBean>) {
        factory.newThread {
            shoppingCartDao.delete(list)
        }.start()
    }

    fun update(merchandiseBean: MerchandiseBean) {
        factory.newThread {
            shoppingCartDao.update(merchandiseBean)
        }.start()
    }
}