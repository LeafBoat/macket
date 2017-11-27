package com.qi.market.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by Qi on 2017/11/13.
 */
class HistoryView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    var horizontalPadding = (context.resources.displayMetrics.density * 10).toInt()
    var verticalPadding = (context.resources.displayMetrics.density * 10).toInt()

    constructor(context: Context) : this(context, null)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }


    override fun measureChild(child: View, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec)
        if (child.measuredWidth + horizontalPadding + horizontalPadding + paddingRight + paddingLeft > measuredWidth) {
            var width = measuredWidth - horizontalPadding - horizontalPadding - paddingLeft - paddingRight
            val lp = child.layoutParams
            val childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, paddingLeft + paddingRight, lp.width)
            child.measure(MeasureSpec.makeMeasureSpec(width,MeasureSpec.getMode(childWidthMeasureSpec)),
                    getChildMeasureSpec(parentHeightMeasureSpec, paddingTop + paddingBottom, lp.height))
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var width = measuredWidth
        //子View的宽度由每行的子View的宽度加上左边距（horizontalPadding）,没有算上右边距
        //即horizontalPadding + View1.width + horizontalPadding + View2.width + horizontalPadding + View3.width + horizontalPadding + View4.width+...+horizontalPadding + View n.width
        var childrenWidth = horizontalPadding
        var lastHeight = verticalPadding
        for (index in 0 until childCount) {
            var child = getChildAt(index)
            if ((childrenWidth + child.measuredWidth + horizontalPadding + paddingRight + paddingLeft) > width) {
                //如果childrenWidth大于这个View的宽度，则将子View换行展示。
                lastHeight = getChildAt(index - 1).bottom
                child.left = horizontalPadding
                child.top = lastHeight + verticalPadding
            } else {
                child.left = childrenWidth + horizontalPadding
                child.top = lastHeight + verticalPadding
            }
            child.right = child.left + child.measuredWidth
            child.bottom = child.top + child.measuredHeight
            childrenWidth = child.right
        }
    }
}