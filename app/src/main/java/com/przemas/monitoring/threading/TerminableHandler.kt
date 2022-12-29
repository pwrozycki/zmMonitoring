package com.przemas.monitoring.threading

import android.os.Handler

class TerminableHandler {
    private val handler = Handler()
    private var terminated = false
    fun terminate() {
        terminated = true
    }

    fun postDelayed(r: Runnable, delay: Long): Boolean {
        return if (!terminated) {
            handler.postDelayed({
                if (!terminated) {
                    r.run()
                }
            }, delay)
        } else false
    }
}