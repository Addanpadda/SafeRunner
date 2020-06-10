package com.example.saferunner.usecases

import android.location.Location

abstract class RunnerGuard {
    var isActive: Boolean = false

    open fun activate() {
        if (checkRunnability()) isActive = true
    }

    open fun deactivate() {
        isActive = false
    }

    open fun toggleActivation() {
        when (isActive) {
            true -> deactivate()
            false -> activate()
        }
    }

    abstract fun checkRunnability(): Boolean

    fun speedCallback(speed: Float) {
        if (!checkIfAlive(speed)) sendHelpNotification()
    }

    abstract fun checkIfAlive(speed: Float): Boolean

    abstract fun sendHelpNotification()
}