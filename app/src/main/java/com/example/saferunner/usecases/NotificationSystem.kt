package com.example.saferunner.usecases

interface NotificationSystem {
    fun sendMassage(message: String, receivers: Array<String>)
}