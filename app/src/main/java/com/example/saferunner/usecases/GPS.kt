package com.example.saferunner.usecases

interface GPS {
    val minIntervalUpdateTimeMs: Long

    fun isGPSEnabled(): Boolean
    fun initializeGPS()
    fun freeGPS()
}