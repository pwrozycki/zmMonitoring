package com.przemas.monitoring.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.android.volley.Response
import com.przemas.monitoring.R
import com.przemas.monitoring.model.ConnectionStatus
import com.przemas.monitoring.model.MonitorModel
import com.przemas.monitoring.rest.VolleySingleton
import com.przemas.monitoring.rest.ZoneMinderVolleyClient
import com.przemas.monitoring.tunnel.ConnectionStateListener
import com.przemas.monitoring.tunnel.TunnelKeeper
import com.przemas.monitoring.tunnel.model.Auth
import com.przemas.monitoring.tunnel.model.Host
import com.przemas.monitoring.tunnel.model.SshGateway
import com.przemas.monitoring.tunnel.model.TunnelSpec

class MainActivity : AppCompatActivity(), ConnectionStateListener {
    val model: MonitorModel
        get() = ViewModelProvider(this).get(MonitorModel::class.java)

    private val zmClient: ZoneMinderVolleyClient?
        get() = model.host.value?.let {
            ZoneMinderVolleyClient(it, VolleySingleton.getInstance(this))
        }

    private var tunnelKeeper: TunnelKeeper? = null

    private val tunnelSpec = TunnelSpec(
        arrayListOf(
            SshGateway(
                Auth.PasswordAuth("user1", "password1"),
                "host1", 22
            ),
            SshGateway(
                Auth.PasswordAuth("user2", "password2"),
                "host2", 2222
            )
        ),
        Host("targetHost", 80)
    )

    override fun onPostResume() {
        super.onPostResume()
        (tunnelKeeper ?: createTunnelKeeper()).start()
    }

    private fun createTunnelKeeper() =
        TunnelKeeper.forSpec(tunnelSpec).apply {
            listener = this@MainActivity
            tunnelKeeper = this
        }

    override fun onPause() {
        tunnelKeeper?.terminate()
        super.onPause()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureTabs()
    }

    private fun configureTabs() {
        val viewPager = findViewById<View>(R.id.view_pager) as ViewPager
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(MonitorStatusFragment(), "Monitors")
        viewPagerAdapter.addFragment(MonitorPreviewFragment(), "Previews")
        viewPager.adapter = viewPagerAdapter

    }


    override fun connectionSuccess() {
        model.connectionStatus.value = ConnectionStatus.CONNECTED
        model.host.value = tunnelKeeper?.localForwardHost()
        sendSyncRequests()
    }


    override fun connectionFailure() {
        model.connectionStatus.value = ConnectionStatus.FAILURE
        resetModel()
    }

    override fun connectionAlive() {
        model.connectionStatus.value = ConnectionStatus.ALIVE
        sendSyncRequests()
    }

    override fun connectionDown() {
        model.connectionStatus.value = ConnectionStatus.DISCONNECTED
        resetModel()
    }

    private fun resetModel() {
        model.monitors.value = emptyList()
        model.host.value = null
        model.monitoringStates.value = emptyList()
    }

    private fun sendSyncRequests() {
        zmClient?.monitorInfos(Response.Listener {
            model.monitors.value = it.monitors!!.map { it.Monitor!! }
        })
        zmClient?.monitoringStates(Response.Listener {
            model.monitoringStates.value = it.states!!.map { it.State!! }
        })
    }
}