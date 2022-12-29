package com.przemas.monitoring.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.przemas.monitoring.R
import com.przemas.monitoring.model.MonitorModel
import com.przemas.monitoring.rest.model.Monitor
import com.przemas.monitoring.ui.views.MonitorPreviewView
import java.util.*
import kotlin.jvm.internal.Intrinsics

class MonitorPreviewFragment :
    Fragment(R.layout.fragment_previews), OnScrollChangedListener,
    OnGlobalLayoutListener {

    private var paused = false
    private val previewViews = HashMap<String, MonitorPreviewView>()
    private lateinit var previewsLayout: ViewGroup
    private lateinit var scrollView: ScrollView

    private val model: MonitorModel
        get() = ViewModelProvider(requireActivity()).get(MonitorModel::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        previewsLayout = view.findViewById(R.id.previews_layout)
        scrollView = view.findViewById(R.id.scroll_view)
    }

    override fun onPause() {
        cleanup()
        scrollView.viewTreeObserver.removeOnScrollChangedListener(this)
        scrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        paused = true
        super.onPause()
    }

    private fun cleanup() {
        HashMap(previewViews).keys.forEach { removeView(it) }
    }

    override fun onResume() {
        super.onResume()
        paused = false
        model.monitors.observe(requireActivity(), Observer { setupMonitorPreviewsForMonitors(it) })
//        scrollView.setOnTouchListener(OnTouchListener())
        scrollView.viewTreeObserver.addOnScrollChangedListener(this)
        scrollView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

/*
    private inner class OnTouchListener : View.OnTouchListener {
        private var downSent = false
        private val viewReceivingEvents: MonitorPreviewView? = null

        */
/* JADX WARNING: Code restructure failed: missing block: B:5:0x0016, code lost:
            if (viewReceivingEvents != 3) goto L_0x0033;
         *//*

        */
/* Code decompiled incorrectly, please refer to instructions dump. *//*

        override fun onTouch(v: View, event: MotionEvent): Boolean {
                int  = event.getAction()
                goto L_0x0032
            L_0x0019:
                com.przemas.monitoring.ui.views.MonitorPreviewView viewReceivingEvents = this.viewReceivingEvents
                if (event.action != ACTION_DOWN) {
                    this.forwardTouchEventToPreviewView(viewReceivingEvents, r6)
                }

                this.viewReceivingEvents = this.findClickedPreviewView(event)
                goto L_0x0028
            L_0x0025:
            L_0x0028:
                goto L_0x0032
            L_0x002b:
                viewReceivingEvents = 0
                com.przemas.monitoring.ui.views.MonitorPreviewView viewReceivingEvents = (com.przemas.monitoring.ui.views.MonitorPreviewView) viewReceivingEvents
                this.viewReceivingEvents = viewReceivingEvents
                r4.downSent = r2
            L_0x0032:
                return r2
        }

        private fun forwardTouchEventToPreviewView(
            it: MonitorPreviewView,
            event: MotionEvent
        ): Boolean {
            if (!downSent) {
                val obtain = MotionEvent.obtain(0, 0, ACTION_DOWN, event.x, 100.0f, 0)
                it.invokeWebViewOnTouch(obtain)
                downSent = true
            }

            val obtain = MotionEvent.obtain(0, 0, ACTION_MOVE, event.x, 100.0f, 0)
            return it.invokeWebViewOnTouch(obtain)
        }

        private fun findClickedPreviewView(event: MotionEvent): MonitorPreviewView? {
            previewViews.values.find {
                val rect = Rect()
                it.getGlobalVisibleRect(rect)
                rect.contains(event.rawX.toInt(), event.rawY.toInt())
            }
        }
    }
*/

    override fun onScrollChanged() {
        resumeOrPauseBasedOnVisibility()
    }

    override fun onGlobalLayout() {
        resumeOrPauseBasedOnVisibility()
    }

    private fun setupMonitorPreviewsForMonitors(monitors: List<Monitor>) {
        if (!paused) {
            createMissingWebViews(monitors)
            removeObsoleteViews(monitors)
            resumeOrPauseBasedOnVisibility()
        }
    }

    private fun createMissingWebViews(monitors: List<Monitor>) {
        monitors.filter { it.Name!! !in previewViews.keys }.forEach { monitor ->
            createView(monitor)
        }
    }

    private fun removeObsoleteViews(list: List<Monitor>) {
        val monitorNames = list.map { it.Name!! }
        previewViews.filterKeys { it !in monitorNames }.forEach { (monitorName, _) ->
            removeView(monitorName)
        }
    }

    private fun removeView(monitorName: String) {
        previewViews.remove(monitorName)?.let {
            it.destroy()
            previewsLayout.removeView(it)
        }
    }

    private fun resumeOrPauseBasedOnVisibility() {
        for (monitorName in previewViews.keys) {
            previewViews[monitorName]?.let {
                if (viewInScrollArea(it)) {
                    it.resume()
                } else {
                    it.stop()
                }
            }
        }
    }

    private fun viewInScrollArea(it: MonitorPreviewView) = Rect().apply {
        it.getHitRect(this)
    }.intersect(
        Rect(
            scrollView.scrollX,
            scrollView.scrollY,
            scrollView.scrollX + scrollView.width,
            scrollView.scrollY + scrollView.height
        )
    )

    private fun createView(monitor: Monitor) {
        MonitorPreviewView(
            requireActivity(), monitor, model.host.value!!, displayWidth()
        ).also {
            previewsLayout.addView(it)
            previewViews[monitor.Name!!] = it
        }
    }

    private fun displayWidth(): Int {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }
}