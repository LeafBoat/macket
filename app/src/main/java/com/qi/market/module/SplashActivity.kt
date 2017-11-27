package com.qi.market.module

import android.content.Intent
import android.os.Bundle
import com.qi.market.R
import com.qi.market.base.BaseActivity
import com.qi.market.common.SharePreferenceHelper
import com.qi.market.module.login.activity.LoginActivity
import com.qi.market.module.main.activity.MainActivity

/**
 * 欢迎界面
 * Created by Qi on 2017/11/18.
 */
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Thread({
            Thread.sleep(500)
            startActivity(Intent(
                    SplashActivity@ this,
                    if (SharePreferenceHelper.getUserBean(SplashActivity@ this) == null) LoginActivity::class.java else MainActivity::class.java
            ))
            finish()
        }).start()
    }
}