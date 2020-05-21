package com.example.saferunner

// GPS interface
import com.example.saferunner.usecases.GPS

// Location datastructure

// GPS service


// Debug
import android.util.Log

// Android necessities
import android.content.Context
import android.location.*
import android.os.Bundle


class GPS (context: Context) : GPS {
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var speed: Float? = null
        get() = locationListener?.speed
    var latitude: Double? = null
        get() = locationListener?.latitude
    var longitude: Double? = null
        get() = locationListener?.longitude

    // LocationListener's constants
    override val minIntervalUpdateTimeMs: Long = 10000 // 10 sec
    var locationListener: GPSLocationListener? = null

    override fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun initializeGPS() {
        // TODO: Check permissions here!
        locationListener = GPSLocationListener()
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            minIntervalUpdateTimeMs, 0f, locationListener)
    }

    override fun freeGPS() {
        locationManager.removeUpdates(locationListener)
        speed = null
        latitude = null
        longitude = null
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
}

class GPSLocationListener : LocationListener {
    private var lastLocation: Location? = null
    private var isProviderEnabled: Boolean = true
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var speed: Float = 0f

    var onLocationChangeCallback: ((location: Location, lastLocation: Location) -> Unit)? = null

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

            onLocationChangeCallback?.invoke(location, lastLocation!!)
        }

        lastLocation = location
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("Not yet implemented")
        isProviderEnabled = true
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("Not yet implemented")
        isProviderEnabled = false
    }

    fun setOnLocationChangeCallbackFun(callbackFun: (location: Location, lastLocation: Location) -> Unit) {
        onLocationChangeCallback = callbackFun
    }
}