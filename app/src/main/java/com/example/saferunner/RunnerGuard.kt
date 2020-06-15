package com.example.saferunner

// Android necessities
import android.content.Context
import android.location.Location
import androidx.preference.PreferenceManager.getDefaultSharedPreferences

// RunnerGuard interface
import com.example.saferunner.usecases.RunnerGuard

// Debug
import android.util.Log


class RunnerGuard(context: Context) : RunnerGuard() {
    private var setStatus: ((statusText: String, statusType: StatusType)->Unit)? = null

    private var notAliveSpeedCount = 0
    private val sharedPreferences = getDefaultSharedPreferences(context)
    private val helpMessageReceiverKey = context.getString(R.string.preferences_notification_message_key)
    private val helpMessageReceiver: String?
        get() =  sharedPreferences.getString(helpMessageReceiverKey, "")

    private val maximumStillTimeKey = context.getString(R.string.preferences_maximum_still_time_key)
    private val defaultMaximumStillTime = context.getString(R.string.default_maximum_still_time)
    private val maximumStillTime: Int
        get() = (sharedPreferences.getString(maximumStillTimeKey, defaultMaximumStillTime)!!.toInt() * 1000 /  // milli-sec to sec
                gps.updateIntervalMs).toInt()

    private val aliveSpeedThresholdKey = context.getString(R.string.preferences_speed_threshold_key)
    private val defaultAliveSpeedThreshold = context.getString(R.string.default_speed_threshold)
    private val aliveSpeedThreshold: Float
        get () = sharedPreferences.getString(aliveSpeedThresholdKey, defaultAliveSpeedThreshold)!!.toFloat()

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

        if (!isReceiverInPreferences()) {
            setStatus?.invoke("No receiver in preferences", StatusType.ERROR)
            return false
        }
        else if (!gps.isGPSEnabled()) {
            setStatus?.invoke("GPS turned off", StatusType.ERROR)
            return false
        }

        return true
    }

    private fun isReceiverInPreferences(): Boolean {
        return helpMessageReceiver != ""
    }

    private fun initializeGPS() {
        gps.initializeGPS()
        gps.locationListener?.setOnLocationChangeCallbackFun({ location -> speedCallback(location.speed) })
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
        when(isActive) {
            true -> deactivate()
            false -> activate()
        }
    }

    override fun checkIfAlive(speed: Float): Boolean {
        if (speed < aliveSpeedThreshold) {
            notAliveSpeedCount++
            setStatus?.invoke("LOW SPEED: $speed", StatusType.WARNING)
        } else if (notAliveSpeedCount > 0) {
            notAliveSpeedCount = 0
            setStatus?.invoke("", StatusType.INFORMATION)
        }

        return notAliveSpeedCount != maximumStillTime

    }

    override fun sendHelpNotification() {
        setStatus?.invoke("SENDING SMS!", StatusType.ERROR)

        // SMS max length is 160 characters
        sms.sendMassage(
            "My RunnerGuard is calling for help!\n" +
                    "Maps location: ${gps.googleMapsLocationURL()}", arrayOf(helpMessageReceiver!!))
    }

    fun setStatusCallback(statusCallback: (statusText: String, statusType: StatusType) -> Unit) {
        setStatus = statusCallback
    }
}