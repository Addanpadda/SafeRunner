package com.example.saferunner

// Usual android necessities
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.Context
import android.graphics.Color
import android.location.LocationListener
import android.os.Bundle
import android.view.View

// Debugging
import android.util.Log

// GPS module
import com.example.saferunner.GPS
import com.example.saferunner.RunnerGuard

// Satisfy permissions
import com.example.saferunner.PermissionHandler

// StatusType enum class
import com.example.saferunner.StatusType

import android.location.LocationManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    lateinit var runnerGuard: RunnerGuard
    lateinit var permissionHandler: PermissionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        var permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        ActivityCompat.requestPermissions(this, permissions, 1)
*/



        permissionHandler = PermissionHandler(this)
        permissionHandler.requirePermissions()

        runnerGuard = RunnerGuard(applicationContext)
        /*
        runnerGuard.setStatusCallback {
            setStatusText(it)
        }*/
        runnerGuard.setStatusCallback { statusText, statusType -> setStatusText(statusText, statusType) }


        ////var gps = GPS(getApplicationContext())
        ////Log.d("GPS", gps.getSpeed().toString())


        //var gps = GPS(this)
        //gps.getSpeed()
        //Log.d("SPEED", gps.getSpeed().toString())



        //ActivityCompat.requestPermissions(this, arrayOf("android.permission.ACCESS_FINE_LOCATION"), 1)
        //ActivityCompat.requestPermissions(this, arrayOf("android.permission.ACCESS_COARSE_LOCATION"), 1)
        //ActivityCompat.requestPermissions(this, arrayOf("android.permission.ACCESS_BACKGROUND_LOCATION"), 1)
        //Log.d("Permissions", ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION).toString())


        //var locationManager: LocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //var gps = GPS(locationManager)

    }

    fun setStatusText(statusText: String, statusType: StatusType) {
        (findViewById<View>(R.id.status_text_view) as TextView).setText(statusText)
        when (statusType) {
            StatusType.ERROR -> (findViewById<View>(R.id.status_text_view) as TextView).setTextColor(
                Color.RED)
            StatusType.INFORMATION -> (findViewById<View>(R.id.status_text_view) as TextView).setTextColor(
                Color.GREEN)
        } // TODO: Remove these redundant findViewById calls and do not use when
    }

    fun activateRunnerGuard(view: View) {
        Log.d("DEBUG", "RAN!")
        runnerGuard.activate()
    }
}