package com.qi.market.common.db

import android.content.ContentValues
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
        try {
            for (bean in orderBeans) {
                val values = ContentValues()
                values.put("userid", bean.userid)
                values.put("productid", bean.productid)
                values.put("num", bean.num)
                values.put("producttile", bean.producttile)
                values.put("productprice", bean.productprice)
                db.insert(DB_NAME, null, values)
               /* db.execSQL("insert into $DB_NAME " +
                        "( userid,productid,num,producttile,productprice) values " +
                        "(${bean.userid},${bean.productid},${bean.num},${bean.producttile},${bean.productprice} )")*/
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun delete(productid: Int) {
        var db = mHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(DB_NAME, "PRODUCTID=?", arrayOf(productid.toString()))
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun update(orderBean: OrderDetailBean) {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        try {
            val contentValues = ContentValues()
            contentValues.put("userid",orderBean.userid)
            contentValues.put("num",orderBean.num)
            contentValues.put("producttile",orderBean.producttile)
            contentValues.put("productprice",orderBean.productprice)
            db.update(DB_NAME,contentValues,"productid=?", arrayOf(orderBean.productid.toString()))
            /*val sql = "UPDTAE $DB_NAME " +
                    "SET userid=${orderBean.userid}, num=${orderBean.num},producttile=${orderBean.producttile},productprice=${orderBean.productprice} " +
                    "WHERE productid=${orderBean.productid}"
            db.execSQL(sql)*/
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun query(productid: Int): OrderDetailBean? {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        var orderDetailBean: OrderDetailBean? = null
        try {
            val sql = "select * from $DB_NAME where productid=?"
            var cursor = db.rawQuery(sql, arrayOf(productid.toString()))
            if (cursor.moveToNext()) {
                orderDetailBean = OrderDetailBean()
                orderDetailBean.productid = productid.toLong()
                orderDetailBean.userid = cursor.getLong(cursor.getColumnIndex("userid"))
                orderDetailBean.producttile = cursor.getString(cursor.getColumnIndex("producttile"))
                orderDetailBean.num = cursor.getInt(cursor.getColumnIndex("num")).toLong()
                orderDetailBean.productprice = cursor.getDouble(cursor.getColumnIndex("productprice"))
            }
            db.setTransactionSuccessful()
            return orderDetailBean
        } catch (e: Exception) {
            e.printStackTrace()
            return orderDetailBean
        } finally {
            db.endTransaction()
        }
    }
}