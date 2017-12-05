package com.qi.market.module

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.common.Constant.DB_NAME
import com.qi.market.common.Constant.SP_USER
import com.qi.market.common.SharePreferenceHelper
import com.qi.market.module.login.activity.LoginActivity
import kotlinx.android.synthetic.main.activity_exit.*
import java.io.File

/**
 * Author:liuqi
 * Date:2017/12/5 10:03
 * Detail:
 */
class ExitActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exit)
        backView.setOnClickListener {
            onBackPressed()
        }
        exitView.setOnClickListener {
            SharePreferenceHelper.removeUserBean(it.context)
            startActivity(Intent(it.context, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            deleteDatabase(DB_NAME)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                deleteSharedPreferences(SP_USER)
            } else {
                SharePreferenceHelper.removeUserBean(this)
            }
            finish()
        }
    }
}