package com.example.saferunner

// GPS interface
import android.app.Activity
import com.example.saferunner.usecases.GPS

// Location datastructure
import com.example.saferunner.entities.Position

// GPS service


// Debug
import android.util.Log

// Android necessities
import android.content.Context
import android.location.*


class GPS (context: Context) : GPS {
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var provider: LocationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
    private var location: Location = Location(provider.toString())

    fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun getSpeed(): Float {
        Log.d("Speed", "Getting speed...")
        //Log.d("LONG", locationManager.getLastKnownLocation("gps").time.toString())
        //Log.d("LONG", Location("network").latitude.toString())
        return location.speed
    }
}