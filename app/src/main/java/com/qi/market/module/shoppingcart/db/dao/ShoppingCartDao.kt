package com.qi.market.module.shoppingcart.db.dao

import android.content.ContentValues
import android.content.Context
import com.qi.market.common.Constant
import com.qi.market.module.main.bean.MerchandiseBean
import com.qi.market.module.shoppingcart.db.ShoppingCartSQLiteOpenHelper
import com.qi.market.module.shoppingcart.db.ShoppingCartSQLiteOpenHelper.Companion.DB_NAME

/**
 * 购物车数据库操作
 * Created by Qi on 2017/11/26.
 */
class ShoppingCartDao(context: Context) {
    val mHelper = ShoppingCartSQLiteOpenHelper(context)
    /**
     * 添加商品
     */
    fun insert(merchandiseList: List<MerchandiseBean>) {
        var db = mHelper.writableDatabase
        db.beginTransaction()
        try {
            for (bean in merchandiseList) {
                val values = ContentValues()
                values.put("id", bean.id)
                values.put("typeid", bean.typeid)
                values.put("title", bean.title)
                values.put("description", bean.description)
                values.put("price", bean.price)
                values.put("num", bean.num)
                values.put("picpath", bean.picpath.substring(Constant.BASE_URL.length))
                db.insert(DB_NAME, null, values)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun delete(id: Long) {
        var db = mHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(DB_NAME, "id=?", arrayOf(id.toString()))
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun delete(list:List<MerchandiseBean>){
        var db = mHelper.writableDatabase
        db.beginTransaction()
        try {
            for (bean in list){
                db.delete(DB_NAME, "id=?", arrayOf(bean.id.toString()))
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }

    }

    fun update(merchandiseBean: MerchandiseBean) {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        try {
            val contentValues = ContentValues()
            contentValues.put("num", merchandiseBean.num)
            contentValues.put("typeid", merchandiseBean.typeid)
            contentValues.put("title", merchandiseBean.title)
            contentValues.put("description", merchandiseBean.description)
            contentValues.put("price", merchandiseBean.price)
            contentValues.put("picpath", merchandiseBean.picpath.substring(Constant.BASE_URL.length))
            db.update(DB_NAME, contentValues, "id=?", arrayOf(merchandiseBean.id.toString()))
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            throw e
        } finally {
            db.endTransaction()
        }
    }

}