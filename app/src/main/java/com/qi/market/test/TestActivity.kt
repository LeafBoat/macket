package com.qi.market.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.qi.market.R
import com.qi.market.common.db.main.SQLiteDao
import com.qi.market.module.shoppingcart.db.ShoppingCartSQLiteOpenHelper
import kotlinx.android.synthetic.main.activity_test.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        showError.setOnClickListener {
            //            dataContainerView.showErrorView()
            var dao = SQLiteDao.Builder().setSQLiteOpenHelper((ShoppingCartSQLiteOpenHelper(this))).build()
            var testService = dao.create(TestService::class.java)
            var observable = testService.query()
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ it ->
                for (bean in it) {
                    Toast.makeText(this, bean.title, Toast.LENGTH_SHORT).show()
                }
            }, {
                it.printStackTrace()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }, {
                Toast.makeText(this, "完成", Toast.LENGTH_SHORT).show()
            })
        }
        showProgress.setOnClickListener {
            dataContainerView.showProgressBar()
        }
        showData.setOnClickListener {
            dataContainerView.showDataView()
        }
    }


}
