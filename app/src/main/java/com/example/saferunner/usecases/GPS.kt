package com.example.saferunner.usecases

interface GPS {
    val updateIntervalMs: Long
    var speed: Float?
    var latitude: Double?
    var longitude: Double?


    fun isGPSEnabled(): Boolean
    fun initializeGPS()
    fun freeGPS()
}