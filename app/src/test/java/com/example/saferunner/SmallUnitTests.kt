package com.example.saferunner

// Unit testing dependencies
import org.junit.Test
import org.junit.Assert.*


class SmallUnitTests {
    @Test
    fun runnerGuardIsNotRunningByDefaultTest() {
        var runnerGuard = TestRunnerGuard()

        assertFalse(runnerGuard.isActive)
    }

    @Test
    fun runnerGuardActivateTest() {
        var runnerGuard = TestRunnerGuard()
        runnerGuard.activate()

        assertTrue(runnerGuard.isActive)
    }

    @Test
    fun runnerGuardDeactivateTest() {
        var runnerGuard = TestRunnerGuard()
        runnerGuard.activate()
        runnerGuard.deactivate()

        assertFalse(runnerGuard.isActive)
    }

    @Test
    fun runnerGuardToggleTest() {
        var runnerGuard = TestRunnerGuard()

        runnerGuard.toggleActivation()
        assertTrue(runnerGuard.isActive)
        runnerGuard.toggleActivation()
        assertFalse(runnerGuard.isActive)
    }

    @Test
    fun runnerGuardCheckRunnabilityTest() {
        var runnerGuard = TestRunnerGuard()
        assertTrue(runnerGuard.checkRunnability())
    }

    @Test
    fun sendingSMSTest() {
        var SMS = TestSMS()

        var message = "Message"
        var receivers = arrayOf("3", "1", "2")
        SMS.sendMassage(message, receivers)

        for (receiver in receivers) {
            assertEquals(SMS.sentMessages[receiver], message)
        }
    }

    @Test
    fun isGPSEnabledTest() {
        var GPS = TestGPS()
        assertTrue(GPS.isGPSEnabled())
    }

    @Test
    fun initializeGPSTest() {
        var GPS = TestGPS()
        GPS.initializeGPS()
        assertTrue(GPS.isInitialized)
    }

    @Test
    fun freeGPSTest() {
        var GPS = TestGPS()
        GPS.initializeGPS()
        GPS.freeGPS()
        assertFalse(GPS.isInitialized)
    }
}