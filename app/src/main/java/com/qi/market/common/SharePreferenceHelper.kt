package com.qi.market.common

import android.content.Context
import android.os.Build
import com.google.gson.Gson
import com.qi.market.common.Constant.SP_USER
import com.qi.market.module.login.bean.UserBean

/**
 * 缓存
 * Created by Qi on 2017/11/27.
 */
class SharePreferenceHelper {
    companion object {
        private val USER_INFO = "USER_INFO"
        fun saveUserBean(context: Context, userBean: UserBean) {
            val sp = context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE)
            val editor = sp.edit()
            editor.putString(USER_INFO, Gson().toJson(userBean)).apply()
        }

        fun getUserBean(context: Context): UserBean? {
            val sp = context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE)
            val s = sp.getString(USER_INFO, "")
            return if (s.isNullOrEmpty())
                null
            else
                Gson().fromJson(s, UserBean::class.java)
        }

        fun removeUserBean(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.deleteSharedPreferences(USER_INFO)
            } else {
                val sharedPreferences = context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.clear().apply()
            }
        }
    }
}