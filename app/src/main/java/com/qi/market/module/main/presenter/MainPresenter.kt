package com.qi.market.module.main.presenter

import com.qi.market.module.main.activity.MainActivity
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.main.bean.MerchandiseCategoryBean
import com.qi.market.module.main.model.MainService
import com.qi.market.module.shoppingcart.db.dao.ShoppingCartDao
import com.qi.market.network.RetrofitClient
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
    private var mThreadFactory = Executors.defaultThreadFactory()!!
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
                    mActivity.mMerchandiseCategoryId = list[0].id!!
                    merchandiseList(list[0])//
                    mActivity.fragmentShowProgressbar()//商品列表展示progressbar
                }, {
                    it.printStackTrace()
                })
    }

    /**
     * 获取指定类别的商品列表
     */
    fun merchandiseList(merchandiseCategoryBean: MerchandiseCategoryBean) {
        service.getMerchandise(merchandiseCategoryBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list: List<MerchandiseBean> ->
                    if (list == null || list.isEmpty()) {
                        mActivity.fragmentShowErrorView()
                    } else {
                        mActivity.fragmentShowContentView()
                        mActivity.refreshList(list,merchandiseCategoryBean.id)
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
        mThreadFactory.newThread {
            var bean = mDao.query(merchandiseBean.id!!)
            if (bean == null) {
                var list = ArrayList<MerchandiseBean>()
                list.add(merchandiseBean)
                mDao.insert(list)
            } else {
                mDao.update(merchandiseBean)
            }
            mActivity.runOnUiThread { }
        }.start()
    }

    /**
     * 查询数据库，修改商品在购物车的数量
     */
    fun changeMerchandiseCheckedNum(data: List<MerchandiseBean>,onQueryFinished:()->Unit) {
        mThreadFactory.newThread {
            for (bean in data) {
                var query = mDao.query(bean.id!!)
                if (query != null)
                    bean.num = query.num
            }
            mActivity.runOnUiThread({
                onQueryFinished.invoke()
            })
        }.start()
    }
}


