package com.przemas.monitoring.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Switch
import com.przemas.monitoring.R
import com.przemas.monitoring.rest.model.Monitor

class MonitorSwitchView(
    context: Context?,
    monitor: Monitor,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    private var monitorSwitch: Switch

    var monitor: Monitor = monitor
        set(value) {
            field = value
            synchronizeUI()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_monitor_switch, this, true)
        monitorSwitch = findViewById(R.id.monitor_switch)
        synchronizeUI()
    }

    fun synchronizeUI() {
        monitorSwitch.apply {
            text = monitor.Name + " (" + monitor.Function + ')'
            isChecked = monitor.Enabled == "1"
        }
    }

    fun setSwitchOnClickListener(listener: OnClickListener) {
        monitorSwitch.setOnClickListener(listener)
    }

}