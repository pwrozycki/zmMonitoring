package com.przemas.monitoring.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.przemas.monitoring.R
import com.przemas.monitoring.rest.model.Monitor
import com.przemas.monitoring.tunnel.model.Host
import kotlin.jvm.internal.Intrinsics

class MonitorPreviewView(
    context: Context,
    private var monitor: Monitor,
    var host: Host,
    preferredWidth: Int,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var monitorLabel: TextView
    private var webView: MyWebView

    private var stopped = false

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_monitor_preview, this, true)
        monitorLabel = findViewById(R.id.monitor_preview_label)
        monitorLabel.text = monitor.Name

        webView = findViewById<MyWebView>(R.id.monitor_preview_web_view).apply {
            loadUrl(previewUrl)
        }

        val rotate = monitor.Orientation?.takeIf { !it.isEmpty() }?.let { it.toInt() % 180 != 0 } ?: false

        val height = if (rotate) monitor.Width!! else monitor.Height!!
        val width = if (rotate) monitor.Height!! else monitor.Width!!

        val preferredHeight = height.toInt() * preferredWidth / width.toInt()

        findViewById<SpaceView>(R.id.monitor_preview_space).apply {
            minWidth = preferredWidth;
            minHeight = preferredHeight
        }
    }

    fun invokeWebViewOnTouch(ev: MotionEvent?): Boolean {
        Intrinsics.checkParameterIsNotNull(ev, "ev")
        return webView.onTouchEvent(ev)
    }

    private val previewUrl: String
        get() = "http://" + host.hostname + ':' + host.port + "/zm/cgi-bin" + "/nph-zms?scale=50&mode=jpeg&maxfps=1&buffer=10&&monitor=" + monitor.Id

    fun stop() {
        if (!stopped) {
            webView.loadUrl("about:blank")
            stopped = true
        }
    }

    fun resume() {
        if (stopped) {
            webView.loadUrl(previewUrl)
            stopped = false
        }
    }

    fun destroy() {
        webView.clearHistory()
        webView.clearCache(true)
        webView.destroy()
    }
}