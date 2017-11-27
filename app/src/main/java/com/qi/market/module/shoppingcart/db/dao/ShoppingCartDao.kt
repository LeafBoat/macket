package com.qi.market.module.shoppingcart.db.dao

import android.content.ContentValues
import android.content.Context
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
        for (bean in merchandiseList) {
            val values = ContentValues()
            values.put("id",bean.id)
            values.put("typeid",bean.typeid)
            values.put("title",bean.title)
            values.put("description",bean.description)
            values.put("price",bean.price)
            values.put("num",bean.num)
            values.put("picpath",bean.picpath)
            db.insert(DB_NAME,null,values)
        }
        db.setTransactionSuccessful()
    }

    fun delete(id: Int) {
        var db = mHelper.writableDatabase
        db.beginTransaction()
        db.delete(DB_NAME, "id=?", arrayOf(id.toString()))
        db.setTransactionSuccessful()
    }

    fun update(merchandiseBean: MerchandiseBean) {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        val sql = "UPDTAE $DB_NAME " +
                "SET num=${merchandiseBean.num},typeid=${merchandiseBean.typeid},title=${merchandiseBean.title},description=${merchandiseBean.description},price=${merchandiseBean.price},picpath=${merchandiseBean.picpath}" +
                "WHERE id=${merchandiseBean.id}"
        db.execSQL(sql)
        db.setTransactionSuccessful()
    }

    fun query(id: Long): MerchandiseBean? {
        val db = mHelper.writableDatabase
        db.beginTransaction()
        val sql = "select * from $DB_NAME where id=?"
        var cursor = db.rawQuery(sql, arrayOf(id.toString()))
        if (cursor.moveToNext()) {
            var merchandiseBean = MerchandiseBean()
            merchandiseBean.id = cursor.getInt(cursor.getColumnIndex("id")).toLong()
            merchandiseBean.typeid = cursor.getInt(cursor.getColumnIndex("typeid")).toLong()
            merchandiseBean.title = cursor.getString(cursor.getColumnIndex("title"))
            merchandiseBean.description = cursor.getString(cursor.getColumnIndex("description"))
            merchandiseBean.price = cursor.getDouble(cursor.getColumnIndex("price"))
            merchandiseBean.num = cursor.getInt(cursor.getColumnIndex("num"))
            merchandiseBean.picpath = cursor.getString(cursor.getColumnIndex("picpath"))
            return merchandiseBean
        }
        return null
    }
}