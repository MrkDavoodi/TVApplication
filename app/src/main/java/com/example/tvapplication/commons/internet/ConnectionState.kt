package com.example.tvapplication.commons.internet

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}