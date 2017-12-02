package com.qi.market.module.main.presenter

import com.qi.market.common.db.main.SQLiteDao
import com.qi.market.module.main.activity.MainActivity
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.main.bean.MerchandiseCategoryBean
import com.qi.market.module.main.db.service.MerchandiseService
import com.qi.market.module.main.model.MainService
import com.qi.market.module.shoppingcart.db.ShoppingCartSQLiteOpenHelper
import com.qi.market.module.shoppingcart.db.dao.ShoppingCartDao
import com.qi.market.network.retrofit.RetrofitClient
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

/**
 * 主页逻辑控制
 * Created by Qi on 2017/11/11.
 */
class MainPresenter(activity: MainActivity) {
    private val mActivity = activity
    private var service = RetrofitClient.create(MainService::class.java)
    private val mDao = ShoppingCartDao(mActivity)
    val dao1 = SQLiteDao.Builder().setSQLiteOpenHelper(ShoppingCartSQLiteOpenHelper(activity)).build()
    private var mThreadFactory = Executors.defaultThreadFactory()!!
    var currentCategory: MerchandiseCategoryBean? = null
    var totalNum = 0
    val data: ArrayList<MerchandiseBean> = ArrayList()
    /**
     * 获取商品类别
     */
    fun merchandiseCategory() {
        service.getMerchandiseCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list: List<MerchandiseCategoryBean> ->
                    mActivity.showContentView()
                    mActivity.refreshMenu(list!!)//刷新菜单栏
                    currentCategory = list[0]
                    mActivity.mMerchandiseCategoryId = currentCategory!!.id!!
                    mActivity.setList()
                }, {
                    it.printStackTrace()
                })
    }

    /**
     * 获取指定类别的商品列表
     */
    fun merchandiseList() {
        if (currentCategory == null) return
        service.getMerchandise(currentCategory!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list: List<MerchandiseBean> ->
                    data.clear()
                    if (list == null || list.isEmpty()) {
                        mActivity.fragmentShowErrorView()
                    } else {
                        data.addAll(list)
                        mActivity.fragmentShowContentView()
                        mActivity.refreshList(data, currentCategory!!.id)
                    }
                }, {
                    mActivity.fragmentShowErrorView()
                    it.printStackTrace()
                })

    }

    /**
     * 更新数据库数据
     */
    fun updateShoppingCart(merchandiseBean: MerchandiseBean) {
        var service = dao1.create(MerchandiseService::class.java)
        service.query(merchandiseBean.id!!).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isEmpty()) {
                        var list = ArrayList<MerchandiseBean>()
                        list.add(merchandiseBean)
                        mDao.insert(list)
                    } else {
                        mDao.update(merchandiseBean)
                    }
                }, {
                    it.printStackTrace()
                }, {
                    refreshMerchandiseNum()
                })
    }

    /**
     * 查询数据库，修改商品在购物车的数量
     */
    fun changeMerchandiseCheckedNum(data: List<MerchandiseBean>?, onQueryFinished: () -> Unit) {
        if (data == null) return
        var service = dao1.create(MerchandiseService::class.java)
        for (bean in data) {
            service.query(bean.id!!).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (!it.isEmpty()) {
                            var merchandiseBean = it[0]
                            bean.num = merchandiseBean.num
                        }
                        onQueryFinished.invoke()
                    }, {}, {})
        }
    }

    fun delete(merchandiseBean: MerchandiseBean) {
        mThreadFactory.newThread {
            mDao.delete(merchandiseBean.id!!)
            refreshMerchandiseNum()
        }.start()
    }

    fun refreshMerchandiseNum() {
        mActivity.updateMerchandiseTotalNum()
    }
}


