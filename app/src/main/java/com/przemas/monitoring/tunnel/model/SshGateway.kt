package com.przemas.monitoring.tunnel.model

class SshGateway(var auth: Auth, hostname: String, port: Int) : Host(hostname, port)