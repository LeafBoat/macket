package com.qi.market.common.db

import android.content.Context
import com.qi.market.common.db.OrderSQLiteOpenHelper.Companion.DB_NAME
import com.qi.market.module.shoppingcart.bean.OrderDetailBean

/**
 * Created by Qi on 2017/11/24.
 */
class OrderSQLiteModel(context: Context) {
    private val mHelper = OrderSQLiteOpenHelper(context)

    fun insert(orderBeans: List<OrderDetailBean>) {
        var db = mHelper.writableDatabase
        db.beginTransaction()
        for (bean in orderBeans) {
            db.execSQL("insert into $DB_NAME " +
                    "( userid,productid,num,producttile,productprice) values " +
                    "(${bean.userid},${bean.productid},${bean.num},${bean.producttile},${bean.productprice} )")
        }
        db.setTransactionSuccessful()
    }

    fun delete(productid: Int) {
        var db = mHelper.writableDatabase
        db.beginTransaction()
        db.delete(DB_NAME, "PRODUCTID=?", arrayOf(productid.toString()))
        db.setTransactionSuccessful()
    }

    fun update(orderBean: OrderDetailBean) {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        val sql = "UPDTAE $DB_NAME " +
                "SET userid=${orderBean.userid}, num=${orderBean.num},producttile=${orderBean.producttile},productprice=${orderBean.productprice} " +
                "WHERE productid=${orderBean.productid}"
        db.execSQL(sql)
        db.setTransactionSuccessful()
    }

    fun query(productid: Int): OrderDetailBean? {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        val sql = "select * from $DB_NAME where productid=?"
        var cursor = db.rawQuery(sql, arrayOf(productid.toString()))
        if (cursor.moveToNext()) {
            var orderDetailBean = OrderDetailBean()
            orderDetailBean.productid = productid.toLong()
            orderDetailBean.userid = cursor.getLong(cursor.getColumnIndex("userid"))
            orderDetailBean.producttile = cursor.getString(cursor.getColumnIndex("producttile"))
            orderDetailBean.num = cursor.getInt(cursor.getColumnIndex("num")).toLong()
            orderDetailBean.productprice = cursor.getDouble(cursor.getColumnIndex("productprice"))
            return orderDetailBean
        }
        return null
    }
}