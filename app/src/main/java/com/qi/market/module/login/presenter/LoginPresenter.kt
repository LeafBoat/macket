package com.qi.market.module.login.presenter

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.qi.market.R
import com.qi.market.common.SharePreferenceHelper
import com.qi.market.module.login.activity.LoginActivity
import com.qi.market.module.login.bean.UserBean
import com.qi.market.module.main.activity.MainActivity
import com.qi.market.network.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

/**
 * Created by Qi on 2017/11/22.
 */
class LoginPresenter constructor(activity: LoginActivity) {

    private var mActivity: LoginActivity = activity

    /**
     * 登录
     *
     * @param userBean
     */
    fun login(userBean: UserBean) {
        val service = RetrofitClient.create(LoginService::class.java)
        RetrofitClient.create(service.login(userBean))
                .subscribe({ responseBody ->
                    if (responseBody.flag != null && responseBody.flag!!) {
                        userBean.id = responseBody.id
                        SharePreferenceHelper.saveUserBean(mActivity,userBean)
                        mActivity.startActivity(Intent(mActivity, MainActivity::class.java))
                        mActivity.finish()
                    } else {
                        Toast.makeText(mActivity, responseBody.msg, Toast.LENGTH_SHORT).show()
                    }
                }, { throwable ->
                    throwable.printStackTrace()
                    mActivity.showProgress(false)
                },{
                    mActivity.showProgress(false)
                })
    }

    /**
     * 用户注册
     *
     * @param userBean
     */
    private fun register(userBean: UserBean) {
        var observable = RetrofitClient.create(LoginService::class.java).register(userBean)
        RetrofitClient.create(observable).subscribe({
            Toast.makeText(mActivity, it.msg, Toast.LENGTH_SHORT).show()
            if (!it.flag!!) {
                return@subscribe
            }
            mActivity.changeListView()
        }, {
            it.printStackTrace()
        },{
            mActivity.showProgress(false)
        })
    }

    /**
     * 用户尝试注册
     */
    fun attemptRegister() {
        var focusView: View? = null
        var cancel = false
        if (!isNickValid(mActivity.userBean.username)) {
            mActivity.nickView.error = mActivity.getString(R.string.error_invalid_nick)
            focusView = mActivity.nickView
            cancel = true
        }
        if (!TextUtils.isEmpty(mActivity.userBean.password) && !isPasswordValid(mActivity.userBean.password)) {
            mActivity.passwordView.error = mActivity.getString(R.string.error_invalid_password)
            focusView = mActivity.passwordView
            cancel = true
        }
        if (!isConfirmPasswordValid(mActivity.confirmView.text.toString())) {
            mActivity.confirmView.error = mActivity.getString(R.string.error_invalid_confirm)
            focusView = mActivity.confirmView
            cancel = true
        }
        if (!isEmailValid()) {
            mActivity.emailView.error = mActivity.getString(R.string.error_invalid_email)
            focusView = mActivity.emailView
            cancel = true
        }
        if (!isQQValid()) {
            mActivity.qqView.error = mActivity.getString(R.string.error_invalid_email)
            focusView = mActivity.qqView
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            mActivity.showProgress(true)
            register(mActivity.userBean)
        }
    }

    /**
     * 尝试登录
     */
    fun attemptLogin() {
        var focusView: View? = null
        var cancel = false
        if (!isNickValid(mActivity.userBean.username)) {
            mActivity.nickView.error = mActivity.getString(R.string.error_invalid_nick)
            focusView = mActivity.nickView
            cancel = true
        }
        if (!TextUtils.isEmpty(mActivity.userBean.password) && !isPasswordValid(mActivity.userBean.password)) {
            mActivity.passwordView.error = mActivity.getString(R.string.error_invalid_password)
            focusView = mActivity.passwordView
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            mActivity.showProgress(true)
            login(mActivity.userBean)
        }
    }

    private fun isNickValid(nickname: String): Boolean = !nickname.isNullOrEmpty() && nickname.isNotBlank()

    private fun isPasswordValid(password: String) = password.length > 4

    private fun isConfirmPasswordValid(confirm: String) = !confirm.isNullOrEmpty() && confirm.isNotBlank() && mActivity.userBean.password == confirm && confirm.length > 4
    var regex = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?"
    private fun isEmailValid() = !isNullOrEmptyOrBlank(mActivity.userBean.email) && Pattern.compile(regex).matcher(mActivity.userBean.email).matches()

    private fun isQQValid() = !isNullOrEmptyOrBlank(mActivity.userBean.qq)

    fun isNullOrEmptyOrBlank(string: String): Boolean {
        if (string.isNullOrEmpty())
            return true
        if (string.isBlank())
            return true
        return false
    }
}