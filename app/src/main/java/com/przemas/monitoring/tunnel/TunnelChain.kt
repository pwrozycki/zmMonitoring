package com.przemas.monitoring.tunnel

import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.jcraft.jsch.UserInfo
import com.przemas.monitoring.tunnel.model.Auth
import com.przemas.monitoring.tunnel.model.Auth.PasswordAuth
import com.przemas.monitoring.tunnel.model.Auth.PrvKeyAuth
import com.przemas.monitoring.tunnel.model.Host
import com.przemas.monitoring.tunnel.model.SshGateway
import com.przemas.monitoring.tunnel.model.TunnelSpec
import java.util.*

class TunnelChain(private val tunnelSpec: TunnelSpec) {
    private val jsch = JSch()
    private val localPorts = HashMap<Host, Int>()
    private val sessions = ArrayList<Session>()

    fun setupTunnels(): Boolean {
        for ((gateway, target) in (tunnelSpec.sshGateways + tunnelSpec.destination).zipWithNext()) {
            if (!setupTunnel(gateway as SshGateway, target)) {
                return false
            }
        }
        return true
    }

    fun disconnect() {
        for (session in sessions) {
            session.disconnect()
        }
        reset()
    }

    fun localForwardHost(): Host = localForwardHost(tunnelSpec.destination)

    fun checkAlive(): Boolean = disconnectOnFailure { checkTunnel() }

    private fun disconnectOnFailure(test: () -> Boolean) =
        if (!checkSuccess(test)) {
            disconnect()
            false
        } else {
            true
        }

    private fun checkSuccess(test: () -> Boolean) = try {
        test()
    } catch (e: Exception) {
        false
    }

    private fun checkTunnel(): Boolean {
        sessions.last().openChannel("exec")?.let { channel ->
            if (channel is ChannelExec) {
                channel.setCommand("true")
                channel.connect()
                return true
            }
        }
        return false
    }

    private fun setupTunnel(gateway: SshGateway, target: Host): Boolean = disconnectOnFailure {
        establishSshConnection(gateway)
        setupPortForwarding(sessions.last(), target)
        true
    }

    private fun setupPortForwarding(session: Session, destination: Host): Int {
        val boundPort = session.setPortForwardingL(0, destination.hostname, destination.port)
        localPorts[destination] = Integer.valueOf(boundPort)
        return boundPort
    }

    private fun establishSshConnection(gateway: SshGateway) {
        val host = localForwardHost(gateway)
        val session =
            jsch.getSession(gateway.auth.user, host.hostname, host.port)
        configureSessionAuth(session, gateway.auth)
        session.connect()
        sessions.add(session)
    }

    private fun configureSessionAuth(session: Session, auth: Auth) {
        if (auth is PrvKeyAuth) {
            jsch.removeAllIdentity()
            val bytes = auth.prvKey.toByteArray()
            jsch.addIdentity("identity", bytes, null, null)
            session.setConfig("StrictHostKeyChecking", "no")
            return
        } else if (auth is PasswordAuth) {
            session.userInfo = createPasswordAuth(auth.password)
        }
    }

    private fun reset() {
        sessions.clear()
        localPorts.clear()
    }

    private fun localForwardHost(hop: Host): Host =
        localPorts[hop]?.let { Host("localhost", it) } ?: hop

    private fun createPasswordAuth(password: String): UserInfo = object : UserInfo {
        override fun promptPassphrase(message: String): Boolean = true
        override fun getPassphrase(): String? = null
        override fun getPassword(): String = password
        override fun promptYesNo(message: String): Boolean = true
        override fun showMessage(message: String) = Unit
        override fun promptPassword(message: String): Boolean = true
    }
}