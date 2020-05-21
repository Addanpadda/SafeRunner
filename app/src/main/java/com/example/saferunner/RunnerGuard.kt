package com.example.saferunner

// RunnerGuard interface
import com.example.saferunner.usecases.RunnerGuard

// GPS

// Android necessities
import android.content.Context

// StatusType enum class

// Debug
import android.util.Log

class RunnerGuard(context: Context) : RunnerGuard {
    var setStatus: ((statusText: String, statusType: StatusType)->Unit)? = null
    override var isActive = false
    private val aliveSpeedThreshold = 0.5 // M/S
    private var notAliveSpeedCounts = 0
    private var gps = GPS(context)
    private var sms = SMS()


    override fun activate() {
        Log.d("RunnerGuard", "Activating...")

        if (checkRunnability()) {
            initializeGPS()
            isActive = true
            setStatus?.invoke("Activated Guard", StatusType.INFORMATION)
            Log.d("RunnerGuard", "Activated!")
        }
    }

    override fun checkRunnability(): Boolean {
        // TODO: Check for permissions...
        if (!gps.isGPSEnabled()) {
            setStatus?.invoke("GPS turned off", StatusType.ERROR)
            return false
        }

        return true
    }

    fun initializeGPS() {
        gps.initializeGPS()
        gps.locationListener?.setOnLocationChangeCallbackFun({ location, lastLocation -> checkIfAlive(location.distanceTo(lastLocation)*1000/(location.time - lastLocation!!.time)) })
    }

    override fun deactivate() {
        Log.d("RunnerGuard", "Deactivating...")
        isActive = false
        // TODO: Check if successful with returning boolean
        gps.freeGPS()
        setStatus?.invoke("Deactivated Guard", StatusType.INFORMATION)
    }

    override fun toggleActivation() {
        if (!isActive) {
            activate()
        } else {
            deactivate()
        }
    }

    override fun checkIfAlive(speed: Float) {
        if (speed < aliveSpeedThreshold) {
            notAliveSpeedCounts++
            setStatus?.invoke("LOW SPEED: $speed", StatusType.WARNING)
        } else if (notAliveSpeedCounts > 0) {
            notAliveSpeedCounts = 0
            setStatus?.invoke("", StatusType.INFORMATION)
        }

        if (notAliveSpeedCounts == 2) {
            sendHelpNotification()
        }
    }

    override fun sendHelpNotification() {
        setStatus?.invoke("SENDING SMS!", StatusType.ERROR)
        sms.sendMassage(
            "--------------------------------------------\n" +
                    " My RunnerGuard is calling for help!\n" +
                    " Try calling me before the ambulance though...\n" +
                    " Latitude:  ${gps.latitude}\n" +
                    " Longitude: ${gps.longitude}\n" +
                    " Maps location: ${gps.googleMapsLocationURL()}\n" +
                    "--------------------------------------------", arrayOf())
    }

    fun setStatusCallback(statusCallback: (statusText: String, statusType: StatusType) -> Unit) {
        setStatus = statusCallback
    }
}