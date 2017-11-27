package com.qi.market.base

import android.os.Bundle
import android.support.v4.app.FragmentActivity

abstract class BaseFragmentActivity<MainFragment : BaseFragment,
        ErrorFragment : com.qi.market.base.ErrorFragment,
        LoadingFragment : com.qi.market.base.LoadingFragment> : FragmentActivity() {

    open var mMainFragment: MainFragment? = null
    open var mErrorFragment: ErrorFragment? = null
    open var mLoadingFragment: LoadingFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mErrorFragment = createErrorFragment()
        mLoadingFragment = createLoadingFragment()
        mMainFragment = createMainFragment()
    }

    abstract fun createMainFragment(): MainFragment

    abstract fun createLoadingFragment(): LoadingFragment

    abstract fun createErrorFragment(): ErrorFragment
}
