package com.qi.market.common.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 订单
 * Created by Qi on 2017/11/24.
 */
class OrderSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = "CREATE TABLE $DB_NAME(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, USERID INT, PRODUCTID INT, num INT, PRODUCTTILE TEXT, PRODUCTPRICE REAL" +
                ")"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE IF EXISTS $DB_NAME"
        db?.execSQL(sql)
        onCreate(db)
    }

    companion object {
        private val DB_VERSION = 1
        val DB_NAME = "orders"
    }

}