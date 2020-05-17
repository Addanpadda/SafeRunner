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

    override fun activate() {
        Log.d("RunnerGuard", "Activating...")

        if (checkRunnability()) {
            isActive = true
            Log.d("RunnerGuard", "Activated!")

            //while (isActive) {
            //var handler = android.os.Handler()
            //handler.postDelayed({ checkIfAlive() }, 1000)
            checkIfAlive()
            //}
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

    override fun deactivate() {
        Log.d("RunnerGuard", "Deactivating...")
        isActive = false
    }

    override fun toggleActivation() {
        TODO("Not yet implemented")
    }

    override fun checkIfAlive() {
        if (gps.getSpeed() < aliveSpeedThreathhold) {
            setStatus?.invoke("Speed: " + gps.getSpeed(), StatusType.INFORMATION) // TODO: INFO (Warning) FOR NOW...
        }
    }
    fun setStatusCallback(statusCallback: (statusText: String, statusType: StatusType) -> Unit) {
        setStatus = statusCallback
    }
}