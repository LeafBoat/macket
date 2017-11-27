package com.qi.market.common

import android.content.Context
import com.google.gson.Gson
import com.qi.market.module.login.bean.UserBean

/**
 * Created by Qi on 2017/11/27.
 */
class SharePreferenceHelper {
    companion object {
        private val USER_INFO = "USER_INFO"
        fun saveUserBean(context: Context, userBean: UserBean) {
            var sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            var editor = sp.edit()
            editor.putString(USER_INFO, Gson().toJson(userBean)).commit()
        }

        fun getUserBean(context: Context): UserBean? {
            var sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
            var s = sp.getString(USER_INFO, "")
            return if (s.isNullOrEmpty())
                null
            else
                Gson().fromJson(s, UserBean::class.java)
        }
    }
}