package com.przemas.monitoring.ui.views

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class MyWebView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    WebView(context, attrs, defStyle) {

    init {
        setInitialScale(1)
        settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        scrollY = 0
    }
}