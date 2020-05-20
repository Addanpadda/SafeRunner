package com.example.saferunner

// RunnerGuard interface
import com.example.saferunner.usecases.RunnerGuard

// GPS
import com.example.saferunner.GPS

// Android necessities
import android.app.Activity
import android.content.Context
import android.os.Looper

// StatusType enum class
import com.example.saferunner.StatusType

// Debug
import android.util.Log
import java.util.logging.Handler

class RunnerGuard(context: Context) : RunnerGuard {
    override var isActive = false
    var gps = GPS(context)
    var setStatus: ((statusText: String, statusType: StatusType)->Unit)? = null
    private val aliveSpeedThreathhold = 0.5
    var notAliveSpeedCounts = 0

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
        if (speed < aliveSpeedThreathhold) {
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
        //TODO("Implement this!")
        setStatus?.invoke("SENDING SMS!", StatusType.ERROR)

    }

    fun setStatusCallback(statusCallback: (statusText: String, statusType: StatusType) -> Unit) {
        setStatus = statusCallback
    }
}