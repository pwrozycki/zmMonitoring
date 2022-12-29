package com.przemas.monitoring.tunnel

import com.przemas.monitoring.tunnel.model.TunnelSpec

interface TunnelSpecProvider {
    val tunnelSpec: TunnelSpec?
}