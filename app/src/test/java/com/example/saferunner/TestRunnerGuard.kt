package com.example.saferunner

import com.example.saferunner.usecases.RunnerGuard


class TestRunnerGuard : RunnerGuard() {
    var GPS = TestGPS()
    var SMS = TestSMS()
    var helpMessageReceiver = "12345678"
    var helpMessage = "Help Message"
    var speedThreshold = 0.5f

    override fun checkRunnability(): Boolean {
        return GPS.isGPSEnabled()
    }

    override fun checkIfAlive(speed: Float): Boolean {
        return speed >= speedThreshold
    }

    override fun sendHelpNotification() {
        SMS.sendMassage(helpMessage, arrayOf(helpMessageReceiver))
    }
}