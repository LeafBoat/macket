package com.qi.market.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qi.market.R
import com.qi.market.common.db.OrderSQLiteModel
import com.qi.market.module.shoppingcart.bean.OrderDetailBean
import kotlinx.android.synthetic.main.activity_test.*
import java.util.ArrayList

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        var orderSQLiteModel = OrderSQLiteModel(this)
        var list = ArrayList<OrderDetailBean>()
        for (i in 0 until 10) {
            var orderDetailBean = OrderDetailBean()
            orderDetailBean.productid = i.toLong()
            orderDetailBean.producttile = "${i.hashCode()}"
            list.add(orderDetailBean)
        }

        insertView.setOnClickListener {
            orderSQLiteModel.insert(list)
        }

        deleteView.setOnClickListener {
            orderSQLiteModel.delete(0)
        }
    }


}
