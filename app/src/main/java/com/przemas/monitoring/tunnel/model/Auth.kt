package com.przemas.monitoring.tunnel.model

sealed class Auth constructor(var user: String) {

    class PasswordAuth(user: String, var password: String) : Auth(user)

    class PrvKeyAuth(user: String, var prvKey: String) : Auth(user)
}