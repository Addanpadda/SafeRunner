package com.example.saferunner

// Android necessities
import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences

// RunnerGuard interface
import com.example.saferunner.usecases.RunnerGuard

// Debug
import android.util.Log


class RunnerGuard(var context: Context) : RunnerGuard {
    var setStatus: ((statusText: String, statusType: StatusType)->Unit)? = null
    override var isActive = false
    private val aliveSpeedThreshold = 0.5 // M/S
    private var notAliveSpeedCount = 0
    private val sharedPreferences = getDefaultSharedPreferences(context)
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

    private fun initializeGPS() {
        gps.initializeGPS()
        gps.locationListener?.setOnLocationChangeCallbackFun({ location, lastLocation -> checkIfAlive(location.distanceTo(lastLocation)*1000/(location.time - lastLocation.time)) })
    }

    override fun deactivate() {
        Log.d("RunnerGuard", "Deactivating...")
        isActive = false
        // TODO: Check if successful with returning boolean
        gps.freeGPS()
        notAliveSpeedCount = 0
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
            notAliveSpeedCount++
            setStatus?.invoke("LOW SPEED: $speed", StatusType.WARNING)
        } else if (notAliveSpeedCount > 0) {
            notAliveSpeedCount = 0
            setStatus?.invoke("", StatusType.INFORMATION)
        }

        if (notAliveSpeedCount == 2) {
            sendHelpNotification()
        }
    }

    override fun sendHelpNotification() {
        setStatus?.invoke("SENDING SMS!", StatusType.ERROR)

        // SMS max length 160 characters
        sms.sendMassage(
            "My RunnerGuard is calling for help!\n" +
                    "Maps location: ${gps.googleMapsLocationURL()}", arrayOf(sharedPreferences.getString(
                    context.getString(R.string.preferences_notification_message_key), "")!!))
        // TODO: Make getting number safer ^
    }

    fun setStatusCallback(statusCallback: (statusText: String, statusType: StatusType) -> Unit) {
        setStatus = statusCallback
    }
}