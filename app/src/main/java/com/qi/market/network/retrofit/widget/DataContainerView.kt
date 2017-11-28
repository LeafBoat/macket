package com.qi.market.network.retrofit.widget

import android.content.Context
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import com.qi.market.R
import com.qi.market.widget.DataContainerView.LayoutParams.Type.ERROR
import com.qi.market.widget.DataContainerView.LayoutParams.Type.PROGRESS
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Author:liuqi
 * Date:2017/11/28 11:23
 * Detail:带有进度条和错误页面的View
 */
class DataContainerView : LinearLayout {

    private var progressBar: View? = null
    private var errorView: View? = null
    private var dataView: View? = null


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = VERTICAL
        if (childCount == 0) {
            //如果没有设置progressBar和errorView,生成默认的View
            progressBar = generateDefaultView(PROGRESS)
            errorView = generateDefaultView(ERROR)
        } else if (childCount == 1) {
            //如果有一个子View，判断是否是progressBar或errorView.如果都不是，生成对应的默认View
            var view = getChildAt(0)
            var params = view.layoutParams as LayoutParams
            when {
                params.type == ERROR -> {
                    errorView = view
                    progressBar = generateDefaultView(PROGRESS)
                }
                params.type == PROGRESS -> {
                    progressBar = view
                    errorView = generateDefaultView(ERROR)
                }
                else -> {
                    dataView = view
                    progressBar = generateDefaultView(PROGRESS)
                    errorView = generateDefaultView(ERROR)
                }
            }
        } else if (childCount == 2) {
            //如果有两个子View，判断是否是progressBar或errorView.如果都不是，生成对应的默认View
            for (i in 0 until childCount) {
                var params = getChildAt(i).layoutParams as LayoutParams
                when {
                    params.type == ERROR -> errorView = getChildAt(i)
                    params.type == PROGRESS -> progressBar = getChildAt(i)
                    else -> dataView = getChildAt(i)
                }
            }
            if (errorView == null && progressBar == null) {
                throw Exception("请设置子View的类型")
            }
        } else if (childCount == 3) {
            for (i in 0 until childCount) {
                var params = getChildAt(i).layoutParams as LayoutParams
                when {
                    params.type == ERROR -> errorView = getChildAt(i)
                    params.type == PROGRESS -> progressBar = getChildAt(i)
                    else -> dataView = getChildAt(i)
                }
            }
            if (errorView == null || progressBar == null) {
                throw Exception("请设置子View的类型")
            }
        } else {
            throw Exception("子View的数量不能超过三个")
        }
    }

    /**
     * 生成默认View
     */
    private fun generateDefaultView(type: Int): View {
        if (type != ERROR && type != PROGRESS) {
            throw Exception("View类型错误")
        }
        var view = View.inflate(context, if (type == ERROR) R.layout.layout_error else R.layout.layout_progress, null)
        var params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.type = type
        view.layoutParams = params
        view.visibility = View.GONE
        addView(view)
        return view
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams) {
        if (childCount > 2)
            return
        super.addView(child, index, params)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LinearLayout.LayoutParams = LayoutParams(context, attrs)

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
        dataView?.visibility = View.GONE
    }

    fun showErrorView() {
        errorView?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
        dataView?.visibility = View.GONE
    }


    fun showDataView() {
        dataView?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
        errorView?.visibility = View.GONE
    }

    class LayoutParams : LinearLayout.LayoutParams {
        @TypeMode
        var type = -1

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
            var typedArray = context.obtainStyledAttributes(attrs, R.styleable.DataContainerView)
            type = typedArray.getInt(R.styleable.DataContainerView_type, -1)
        }

        constructor(width: Int, height: Int) : super(width, height)

        @IntDef(ERROR.toLong(), PROGRESS.toLong())
        @Retention(RetentionPolicy.SOURCE)
        annotation class TypeMode

        object Type {
            const val ERROR: Int = 0
            const val PROGRESS: Int = 1
        }
    }
}
