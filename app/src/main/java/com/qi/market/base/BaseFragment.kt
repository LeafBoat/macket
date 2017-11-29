package com.qi.market.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Qi on 2017/11/9.
 */
open class BaseFragment : Fragment() {
    var onAttachListener: (() -> Unit)? = null
    var onCreateListener: (() -> Unit)? = null
    var onCreateViewListener: (() -> Unit)? = null
    var onViewCreatedListener: (() -> Unit)? = null
    var onActivityCreatedListener: (() -> Unit)? = null
    var onStartListener: (() -> Unit)? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (this.onAttachListener != null) {
            onAttachListener!!()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this.onCreateListener != null) {
            onCreateListener!!()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (this.onCreateViewListener != null) {
            onCreateViewListener!!()
        }
        return View(container?.context)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this.onViewCreatedListener != null) {
            onViewCreatedListener!!()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (onActivityCreatedListener != null) {
            onActivityCreatedListener!!()
        }
    }

    override fun onStart() {
        super.onStart()
        if (onStartListener != null) {
            onStartListener!!()
        }
    }


}