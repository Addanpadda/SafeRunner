package com.example.saferunner

import com.example.saferunner.usecases.GPS

class TestGPS : GPS {
    override val updateIntervalMs: Long = 10
    override var speed: Float? = 0f
    override var latitude: Double? = 0.0
    override var longitude: Double? = 0.0
    var isInitialized = false

    override fun isGPSEnabled(): Boolean {
        return true
    }

    override fun initializeGPS() {
        isInitialized = true
    }

    override fun freeGPS() {
        isInitialized = false
    }
}