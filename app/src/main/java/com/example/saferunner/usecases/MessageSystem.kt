package com.example.saferunner.usecases

interface MessageSystem {
    fun sendMassage(message: String, receivers: Array<String>)
}