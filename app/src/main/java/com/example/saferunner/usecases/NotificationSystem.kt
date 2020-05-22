package com.example.saferunner.usecases

interface NotificationSystem {
    fun sendMassage(message: String, receiver: Array<String>)
}