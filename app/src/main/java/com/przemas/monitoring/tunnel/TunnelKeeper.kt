package com.przemas.monitoring.tunnel

import com.przemas.monitoring.threading.MyAsyncTask
import com.przemas.monitoring.threading.SingleTaskExecutor
import com.przemas.monitoring.threading.TerminableHandler
import com.przemas.monitoring.tunnel.model.Host
import com.przemas.monitoring.tunnel.model.TunnelSpec
import java.lang.ref.WeakReference

class TunnelKeeper private constructor(private val tunnelSpec: TunnelSpec) {
    private var executor: SingleTaskExecutor? = null
    private var handler: TerminableHandler? = null
    var listener: ConnectionStateListener? = null
    private var tunnelChain: TunnelChain? = null

    fun terminate() {
        handler!!.terminate()
        tunnelChain!!.disconnect()
        executor!!.cancel()
        listener?.connectionDown()
    }

    fun start() {
        tunnelChain = TunnelChain(tunnelSpec)
        handler = TerminableHandler()
        executor = SingleTaskExecutor()
        createTunnelAsync()
    }

    fun localForwardHost(): Host = tunnelChain!!.localForwardHost()

    private fun scheduleCreateTunnel() {
        handler!!.postDelayed(Runnable { createTunnelAsync() }, 10000)
    }

    private fun createTunnelAsync() {
        val weakThis = WeakReference(this)
        executor!!.executeAndCancelPrevious(
            MyAsyncTask(
                { weakThis.get()?.createTunnel() },
                { success -> weakThis.get()?.createTunnelPostAction(success) }
            )
        )
    }

    private fun createTunnel() = tunnelChain!!.setupTunnels()

    private fun createTunnelPostAction(success: Boolean?) {
        if (success == true) {
            tunnelIsCreated()
        } else {
            tunnelCreationUnsuccessful()
        }
    }

    private fun tunnelIsCreated() {
        listener?.connectionSuccess()
        scheduleCheckTunnelAlive()
    }

    private fun tunnelCreationUnsuccessful() {
        val connectionStateListener = listener
        connectionStateListener?.connectionFailure()
        scheduleCreateTunnel()
    }

    private fun scheduleCheckTunnelAlive() {
        handler!!.postDelayed(Runnable { checkTunnelAliveAsync() }, 10000)
    }

    private fun checkTunnelAliveAsync() {
        val weakThis = WeakReference(this)
        executor!!.executeAndCancelPrevious(
            MyAsyncTask(
                { weakThis.get()?.checkTunnelAlive() },
                { alive -> weakThis.get()?.checkTunnelAlivePostAction(alive) }
            )
        )
    }

    private fun checkTunnelAlive(): Boolean {
        return tunnelChain!!.checkAlive()
    }

    private fun checkTunnelAlivePostAction(alive: Boolean?) {
        if (alive == null) {
            return
        }
        if (alive) {
            tunnelIsAlive()
        } else {
            tunnelHasDied()
        }
    }

    private fun tunnelIsAlive() {
        val connectionStateListener = listener
        connectionStateListener?.connectionAlive()
        scheduleCheckTunnelAlive()
    }

    private fun tunnelHasDied() {
        val connectionStateListener = listener
        connectionStateListener?.connectionDown()
        scheduleCreateTunnel()
    }

    companion object {
        fun forSpec(spec: TunnelSpec): TunnelKeeper = TunnelKeeper(spec)
    }
}