package com.example.saferunner

import android.location.Location
import android.location.LocationManager.PASSIVE_PROVIDER
import org.junit.Test
import org.junit.Assert.*

class LargeUnitTests {
    @Test
    fun sendEmergencyMessageOnLowSpeedTest() {
        var runnerGuard = TestRunnerGuard()
        runnerGuard.activate()

        var mockedSpeed = 0.2f
        runnerGuard.speedCallback(mockedSpeed)

        assertEquals(runnerGuard.SMS.sentMessages[runnerGuard.helpMessageReceiver], runnerGuard.helpMessage)
    }

    @Test
    fun doNotSendEmergencyMessageOnHighSpeedTest() {
        var runnerGuard = TestRunnerGuard()
        runnerGuard.activate()

        var mockedSpeed = 0.6f
        runnerGuard.speedCallback(mockedSpeed)

        assertNotEquals(runnerGuard.SMS.sentMessages[runnerGuard.helpMessageReceiver], runnerGuard.helpMessage)
    }
}