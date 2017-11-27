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

    private var onAttachListener: (() -> Unit)? = null
    private var onCreateListener: (() -> Unit)? = null
    private var onCreateViewListener: (() -> Unit)? = null
    private var onViewCreatedListener: (() -> Unit)? = null
    private var onActivityCreatedListener: (() -> Unit)? = null
    private var onStartListener: (() -> Unit)? = null

    fun setOnAttachListener(onAttachListener: (() -> Unit)?) {
        this.onAttachListener = onAttachListener
    }

    fun setOnCreateListener(onCreateListener: (() -> Unit)?) {
        this.onCreateListener = onCreateListener
    }

    fun setOnCreateViewListener(onCreateViewListener: (() -> Unit)?) {
        this.onCreateViewListener = onCreateViewListener
    }

    fun setOnViewCreatedListener(onViewCreatedListener: (() -> Unit)?) {
        this.onViewCreatedListener = onViewCreatedListener
    }

    fun setOnActivityCreatedListener(onActivityCreatedListener: (() -> Unit)?) {
        this.onActivityCreatedListener = onActivityCreatedListener
    }

    fun setOnStartListener(onStartListener: (() -> Unit)?) {
        this.onStartListener = onStartListener
    }
}