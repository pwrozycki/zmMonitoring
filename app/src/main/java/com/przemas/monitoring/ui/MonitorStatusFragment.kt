package com.przemas.monitoring.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.przemas.monitoring.R
import com.przemas.monitoring.model.ConnectionStatus.*
import com.przemas.monitoring.model.MonitorModel
import com.przemas.monitoring.rest.VolleySingleton
import com.przemas.monitoring.rest.ZoneMinderVolleyClient
import com.przemas.monitoring.rest.model.Monitor
import com.przemas.monitoring.rest.model.MonitoringState
import com.przemas.monitoring.ui.views.MonitorSwitchView
import com.przemas.monitoring.ui.views.MonitoringModesView
import kotlinx.android.synthetic.main.view_monitor_switch.view.*
import kotlin.collections.set


class MonitorStatusFragment : Fragment(R.layout.fragment_monitors) {
    private lateinit var connectionStateTextView: TextView
    private lateinit var monitoringModesLayout: LinearLayout
    private lateinit var monitorSwitchesLayout: LinearLayout

    private var monitoringModesView: MonitoringModesView? = null
    private var monitorSwitchViews: MutableMap<String, MonitorSwitchView> = HashMap()

    private val model: MonitorModel
        get() = ViewModelProvider(requireActivity()).get(MonitorModel::class.java)

    private val zmClient: ZoneMinderVolleyClient?
        get() = model.host.value?.let {
            ZoneMinderVolleyClient(
                it,
                VolleySingleton.getInstance(requireActivity().applicationContext)
            )
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        connectionStateTextView = view.findViewById(R.id.connection_state)
        monitorSwitchesLayout = view.findViewById(R.id.monitor_switches_layout)
        monitoringModesLayout = view.findViewById(R.id.monitoring_modes_layout)
    }

    override fun onPause() {
        super.onPause()
        cleanup()
    }

    override fun onResume() {
        super.onResume()
        configureViewModel()
        setConnectionStateText()
        synchronizeMonitorSwitchViews()
        synchronizeMonitoringModesView()
    }

    private fun setConnectionStateText() {
        model.connectionStatus.value?.let {
            connectionStateTextView.text = when (it) {
                CONNECTED -> getString(R.string.connectionSuccessful)
                ALIVE -> getString(R.string.connectionAlive)
                FAILURE -> getString(R.string.connectionFailure)
                DISCONNECTED -> getString(R.string.connectionDisconnected)
            }
        }
    }

    private fun configureViewModel() {
        model.connectionStatus.observe(requireActivity(),
            Observer {
                when (it) {
                    CONNECTED, ALIVE -> setConnectionStateText()
                    FAILURE, DISCONNECTED -> onConnectionDown()
                }
            })
    }

    private fun onConnectionDown() {
        setConnectionStateText()
        cleanup()
        model.monitoringStates.value = emptyList()
        model.monitors.value = emptyList()
    }

    private fun cleanup() {
        monitorSwitchesLayout.removeAllViews()
        monitoringModesLayout.removeAllViews()
        monitoringModesView = null
        monitorSwitchViews.clear()
    }

    private fun synchronizeMonitoringModesView() {
        model.monitoringStates.observe(requireActivity(),
            Observer { states ->
                getOrCreateMonitoringModesView().apply {
                    monitoringStates = states
                }
            })
    }

    private fun getOrCreateMonitoringModesView(): MonitoringModesView =
        monitoringModesView ?: MonitoringModesView(requireContext()).apply {
            setOnItemSelected { it?.let { clickMonitorSwitchViewsRequiringChange(it) } }
            monitoringModesLayout.addView(this)
            monitoringModesView = this
        }

    private fun clickMonitorSwitchViewsRequiringChange(state: MonitoringState) {
        for (def in retrieveMonitoringModeDefinitions(state)) {
            for (it in monitorSwitchViews.values) {
                if (it.monitor.Id == def.monitorId && it.monitor.Enabled != def.enabled) {
                    it.monitor_switch.performClick()
                }
            }
        }
    }

    private fun retrieveMonitoringModeDefinitions(state: MonitoringState) =
        Regex("(\\d+):\\w+:(\\d+)").findAll(state.Definition!!)
            .map { it.destructured }
            .map { MonitorDefinition(it.component2(), it.component1()) }

    data class MonitorDefinition(var enabled: String, var monitorId: String)

    private fun synchronizeMonitorSwitchViews() {
        model.monitors.observe(requireActivity(),
            Observer { monitors ->
                for (monitor in monitors) {
                    monitorSwitchViews[monitor.Name]?.let { switch ->
                        switch.monitor = monitor
                    } ?: createAddNewMonitorSwitch(monitor)
                }
                removeObsoleteMonitorViews(monitors)
            })
    }

    private fun createAddNewMonitorSwitch(monitor: Monitor) {
        monitorSwitchViews[monitor.Name!!] = createMonitorSwitchView(monitor)
            .also { monitorSwitchesLayout.addView(it) }
    }

    private fun createMonitorSwitchView(monitor: Monitor): MonitorSwitchView {
        return MonitorSwitchView(requireContext(), monitor)
            .apply {
                setSwitchOnClickListener(View.OnClickListener {
                    sendChangeMonitorStateRequest(this)
                })
            }
    }

    private fun sendChangeMonitorStateRequest(monitorSwitchView: MonitorSwitchView) {
        monitorSwitchView.synchronizeUI()
        val monitor = monitorSwitchView.monitor
        val changedMonitor = monitor.copy(Enabled = if (monitor.Enabled == "0") "1" else "0")
        monitorSwitchView.monitor = monitor
        monitorSwitchView.monitor_switch.isEnabled = false
        zmClient?.changeMonitor(
            Monitor(Id = monitor.Id, Enabled = changedMonitor.Enabled),
            listener = Response.Listener {
                monitorSwitchView.monitor = changedMonitor
                monitorSwitchView.monitor_switch.isEnabled = true
            }
        )
    }

    private fun removeObsoleteMonitorViews(monitors: List<Monitor>) {
        val monitorNames = monitors.map { it.Name!! }
        monitorSwitchViews.values.filter { it.monitor.Name !in monitorNames }.forEach {
            monitorSwitchesLayout.removeView(it)
        }
    }
}