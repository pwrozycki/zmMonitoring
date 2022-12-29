package com.przemas.monitoring.rest

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.przemas.monitoring.rest.model.JacksonRequest
import com.przemas.monitoring.rest.model.Monitor
import com.przemas.monitoring.rest.model.MonitorInfosResponseWrapper
import com.przemas.monitoring.rest.model.MonitoringStatesResponseWrapper
import com.przemas.monitoring.tunnel.model.Host

class ZoneMinderVolleyClient(
    private val host: String,
    private val volley: VolleySingleton
) {
    companion object {
        const val CHANGE_MONITOR_REQUEST_TAG = "VolleySingletonchangeMonitorRequest"
        const val MONITOR_INFOS_REQUEST_TAG = "monitorInfosRequest"
        const val MONITOR_STATES_REQUEST_TAG = "monitorStatesRequest"
    }

    constructor(
        host: Host,
        volley: VolleySingleton
    ) : this("http://" + host.hostname + ':' + host.port, volley)

    fun monitorInfos(
        listener: Response.Listener<MonitorInfosResponseWrapper>
    ) {
        val it = JacksonRequest(
            Request.Method.GET,
            host + "/zm/api/monitors.json",
            MonitorInfosResponseWrapper::class.java,
            listener,
            Response.ErrorListener { logError(it) }
        )
        it.tag = MONITOR_INFOS_REQUEST_TAG
        volley.add(it)
    }

    fun changeMonitor(
        monitor: Monitor,
        listener: Response.Listener<String>
    ) {
        val it = JacksonRequest(
            Request.Method.PUT,
            host + "/zm/api/monitors/" + monitor.Id + ".json",
            String::class.java,
            listener,
            Response.ErrorListener { logError(it) },
            monitor
        )
        it.tag = CHANGE_MONITOR_REQUEST_TAG
        volley.add(it)
    }

    fun monitoringStates(
        listener: Response.Listener<MonitoringStatesResponseWrapper>
    ) {
        val it = JacksonRequest(
            Request.Method.GET,
            host + "/zm/api/states.json",
            MonitoringStatesResponseWrapper::class.java,
            listener,
            Response.ErrorListener { logError(it) }
        )
        it.tag = MONITOR_STATES_REQUEST_TAG
        volley.add(it)
    }

    private fun logError(it: VolleyError) {
        Log.e(javaClass.simpleName, it.message!!)
    }

}