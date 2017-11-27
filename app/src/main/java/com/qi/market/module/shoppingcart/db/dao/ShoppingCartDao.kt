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
                values.put("picpath", bean.picpath)
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

    fun query(id: Long): MerchandiseBean? {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        try {
            val sql = "select * from $DB_NAME where id=?"
            var cursor = db.rawQuery(sql, arrayOf(id.toString()))
            var merchandiseBean: MerchandiseBean? = null
            if (cursor.moveToNext()) {
                merchandiseBean = MerchandiseBean()
                merchandiseBean.id = cursor.getInt(cursor.getColumnIndex("id")).toLong()
                merchandiseBean.typeid = cursor.getInt(cursor.getColumnIndex("typeid")).toLong()
                merchandiseBean.title = cursor.getString(cursor.getColumnIndex("title"))
                merchandiseBean.description = cursor.getString(cursor.getColumnIndex("description"))
                merchandiseBean.price = cursor.getDouble(cursor.getColumnIndex("price"))
                merchandiseBean.num = cursor.getInt(cursor.getColumnIndex("num"))
                var picpath = cursor.getString(cursor.getColumnIndex("picpath"))
                if (!picpath.isNullOrEmpty())
                    picpath = Constant.BASE_URL + picpath
                merchandiseBean.picpath = picpath
            }
            db.setTransactionSuccessful()
            return merchandiseBean
        } catch (e: Exception) {
            throw e
        } finally {
            db.endTransaction()
        }
    }

    fun queryAll(): List<MerchandiseBean> {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        var list = ArrayList<MerchandiseBean>()
        try {
            val sql = "select * from $DB_NAME"
            var cursor = db.rawQuery(sql, null)
            while (cursor.moveToNext()) {
                var merchandiseBean = MerchandiseBean()
                merchandiseBean.id = cursor.getInt(cursor.getColumnIndex("id")).toLong()
                merchandiseBean.typeid = cursor.getInt(cursor.getColumnIndex("typeid")).toLong()
                merchandiseBean.title = cursor.getString(cursor.getColumnIndex("title"))
                merchandiseBean.description = cursor.getString(cursor.getColumnIndex("description"))
                merchandiseBean.price = cursor.getDouble(cursor.getColumnIndex("price"))
                merchandiseBean.num = cursor.getInt(cursor.getColumnIndex("num"))
                var picpath = cursor.getString(cursor.getColumnIndex("picpath"))
                if (!picpath.isNullOrEmpty())
                    picpath = Constant.BASE_URL + picpath
                merchandiseBean.picpath = picpath
                list.add(merchandiseBean)
            }
            db.setTransactionSuccessful()
            return list
        } catch (e: Exception) {
            e.printStackTrace()
            return list
        } finally {
            db.endTransaction()
        }
    }
}