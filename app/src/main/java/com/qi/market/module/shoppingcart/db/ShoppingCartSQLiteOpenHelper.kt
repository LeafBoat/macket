package com.qi.market.module.shoppingcart.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.qi.market.common.Constant.DB_NAME
import com.qi.market.common.Constant.T_SHOPPING_CART

/**
 * Created by Qi on 2017/11/26.
 */
class ShoppingCartSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table if not exists $T_SHOPPING_CART(id integer primary key,typeid integer,title text,description text,price double,num integer,picpath text)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE IF EXISTS $T_SHOPPING_CART"
        db?.execSQL(sql)
        onCreate(db)
    }

    companion object {
        val DB_VERSION = 1
    }
}