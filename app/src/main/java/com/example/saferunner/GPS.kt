package com.example.saferunner

// Android necessities
import android.content.Context
import android.location.*
import android.os.Bundle
import android.os.PowerManager

// GPS interface
import com.example.saferunner.usecases.GPS

// Debug
import android.util.Log

class GPS (context: Context) : GPS {
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    override var speed: Float? = null
        get() = locationListener?.speed
    override var latitude: Double? = null
        get() = locationListener?.latitude
    override var longitude: Double? = null
        get() = locationListener?.longitude

    // LocationListener's constants
    override val updateIntervalMs: Long = 10000 // 10 sec
    var locationListener: GPSLocationListener? = null

    // Wakelock for gps in background
    private var powerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    private var wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "RunnerGuard:GPSWakelockTag")

    override fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun initializeGPS() {
        // TODO: Check permissions here!
        wakeLock.acquire()
        locationListener = GPSLocationListener()
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            updateIntervalMs, 0f, locationListener!!)
    }

    override fun freeGPS() {
        locationManager.removeUpdates(locationListener!!)
        speed = null
        latitude = null
        longitude = null
        wakeLock.release()
    }

    fun googleMapsLocationURL(): String {
        if (latitude != null && longitude != null) {
            return generateGoogleMapsLocationURL(latitude!!, longitude!!)
        }
        return String()
    }

    private fun generateGoogleMapsLocationURL(latitude: Double, longitude: Double): String {
        return "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
    }

    
    class GPSLocationListener : LocationListener {
        private var lastLocation: Location? = null
        private var isProviderEnabled: Boolean = true
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var speed: Float = 0f

        var onLocationChangeCallback: ((location: Location) -> Unit)? = null

        override fun onLocationChanged(location: Location?) {

            if (isProviderEnabled &&
                lastLocation != null
                && location != null) {
                Log.d("Lat1", location.latitude.toString())
                Log.d("Long1", location.longitude.toString())
                Log.d("Lat2", lastLocation!!.latitude.toString())
                Log.d("Long2", lastLocation!!.longitude.toString())
                Log.d("DIST", location.distanceTo(lastLocation).toString())
                Log.d("DELTA", (location.time - lastLocation!!.time).toString())
                Log.d("SPEED", (location.distanceTo(lastLocation)*1000/(location.time - lastLocation!!.time)).toString())
                Log.d("SPEED", (location.distanceTo(lastLocation)/((location.time - lastLocation!!.time)/(60*60))).toString())

                latitude = location.latitude
                longitude = location.longitude
                speed = location.distanceTo(lastLocation)*1000/(location.time - lastLocation!!.time)

                location.speed = speed

                onLocationChangeCallback?.invoke(location)
            }

            lastLocation = location
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.d("OnStatusChanged", "Provider: $provider, Status: $status, Extras: $extras")
        }

        override fun onProviderEnabled(provider: String?) {
            //TODO("Not yet implemented")
            isProviderEnabled = true
        }

        override fun onProviderDisabled(provider: String?) {
            //TODO("Not yet implemented")
            isProviderEnabled = false
        }

        fun setOnLocationChangeCallbackFun(callbackFun: (location: Location) -> Unit) {
            onLocationChangeCallback = callbackFun
        }
    }
}