package com.qi.market.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.qi.market.R
import kotlinx.android.synthetic.main.view_title.view.*

/**
 * Created by Qi on 2017/11/15.
 */
class TitleView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    var views = ArrayList<View>()

    init {
        inflate(context, R.layout.view_title, this)
    }

    constructor(context: Context) : this(context, null)

    override fun onFinishInflate() {
        super.onFinishInflate()
        var layoutParams = middleView.layoutParams as LayoutParams
        layoutParams.weight = 1f
        middleView.layoutParams = layoutParams
        views.clear()
        for (i in 0 until childCount) {
            var child = getChildAt(i)
            var params = child.layoutParams as TitleView.LayoutParams
            var index = params.position
            if (index > -1 && index < 3) {
                views.add(child)
            }
        }
        changeViewsPosition()
    }

    private fun changeViewsPosition() {
        var count = views.size
        for (i in 0 until count) {
            var child = views[i]
            var params = child.layoutParams as TitleView.LayoutParams
            var index = params.position
            when (index) {
                0 -> {
                    removeView(child)
                    child.layoutParams = getFrameLayoutParams(params)
                    addViewToLeft(child)
                }
                1 -> {
                    removeView(child)
                    child.layoutParams = getFrameLayoutParams(params)
                    addViewToMiddle(child)
                }
                2 -> {
                    removeView(child)
                    child.layoutParams = getFrameLayoutParams(params)
                    addViewToRight(child)
                }
            }
        }
    }

    private fun getFrameLayoutParams(params: TitleView.LayoutParams): FrameLayout.LayoutParams? {
        var layoutParams = FrameLayout.LayoutParams(params.width, params.height)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.marginEnd = params.marginEnd
            layoutParams.marginStart = params.marginStart
        }
        layoutParams.leftMargin = params.leftMargin
        layoutParams.topMargin = params.topMargin
        layoutParams.rightMargin = params.rightMargin
        layoutParams.bottomMargin = params.bottomMargin
        layoutParams.gravity = Gravity.CENTER
        return layoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LinearLayout.LayoutParams = LayoutParams(context, attrs)


    fun addViewToLeft(view: View) {
        leftView.addView(view)
    }

    fun addViewToMiddle(view: View) {
        middleView.addView(view)
    }

    fun addViewToRight(view: View) {
        rightView.addView(view)
    }


    class LayoutParams : LinearLayout.LayoutParams {

        var position: Int = -1

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            var typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView)
            val N: Int = typedArray.indexCount
            (0 until N)
                    .map { typedArray.getIndex(it) }
                    .filter { it == R.styleable.TitleView_position }
                    .forEach { position = typedArray.getInt(it, -1) }
        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(width: Int, height: Int, weight: Float) : super(width, height, weight)
        constructor(source: ViewGroup.LayoutParams) : super(source)
        constructor(source: ViewGroup.MarginLayoutParams) : super(source)
        constructor(source: LayoutParams) : super(source)
    }
}