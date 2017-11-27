package com.qi.market.module.login.activity

import android.Manifest.permission.READ_CONTACTS
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.qi.market.R
import com.qi.market.module.login.bean.UserBean
import com.qi.market.module.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * 登录状态：0，注册；1，登录
     */
    private var state: Int = 0
    private lateinit var presenter: LoginPresenter
    val userBean: UserBean = UserBean()
    private val userField = arrayOf("username", "password", "", "email", "mobilePhone", "qq")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = LoginPresenter(this)
        bindView()
        //切换登录页的状态
        intentView.setOnClickListener {
            changeListView()
        }
        //登录按钮事件处理
        signView.setOnClickListener {
            if (state == 0) {
                presenter.attemptRegister()
            } else if (state == 1) {
                presenter.attemptLogin()
            }
        }
        signView.requestFocus()
    }

    /**
     * 改变form
     */
    fun changeListView() {
        state = if (state == 0) 1 else 0
        for (i in 0 until listView.childCount) {
            if (state == 0) {
                listView.getChildAt(i).visibility = View.VISIBLE
            } else if (state == 1) {
                if (listView.getChildAt(i).tag == resources.getString(R.string.tag_register)) {
                    //如果单元格属于注册时使用的则让其隐藏
                    var viewGroup = listView.getChildAt(i) as ViewGroup
                    var viewGroup2 = viewGroup.getChildAt(0) as ViewGroup
                    var textView = viewGroup2.getChildAt(0) as TextView
                    textView.text = ""
                    viewGroup.visibility = View.GONE
                }
            }
        }
        if (state == 0) {
            intentView.text = resources.getString(R.string.intent_login)
            signView.text = getString(R.string.action_sign_up)
        } else {
            intentView.text = resources.getString(R.string.intent_register)
            signView.text = getString(R.string.action_sign_in)
        }
    }

    /**
     * 将user与view绑定
     */
    private fun bindView() {
        for (i in 0 until listView.childCount) {
            var view = listView.getChildAt(i)
            if (view is TextInputLayout) {
                var viewGroup = view.getChildAt(0) as ViewGroup
                var textView = viewGroup.getChildAt(0) as TextView
                textView.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (userField[i].isNullOrEmpty()) {
                            return
                        }
                        var field = userBean.javaClass.getDeclaredField(userField[i])
                        field.isAccessible = true
                        field.set(userBean, s.toString())
                    }
                })
            }
        }
    }

    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(nickView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok,
                            { requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS) })
        } else {
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
        return false
    }


    fun dismissProgressbar() {
        progressBar.visibility = View.GONE
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            progressBar.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            progressBar.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }


    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0
    }
}
