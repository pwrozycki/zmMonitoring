package com.przemas.monitoring.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class SpaceView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    View(context, attrs, defStyle) {
    var minWidth: Int = 0
    var minHeight: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            getDimension(widthMeasureSpec, minWidth),
            getDimension(heightMeasureSpec, minHeight)
        )
    }

    private fun getDimension(measureSpec: Int, preferred: Int): Int =
        if (MeasureSpec.getMode(measureSpec) == Int.MIN_VALUE)
            min(MeasureSpec.getSize(measureSpec), preferred)
        else preferred
}