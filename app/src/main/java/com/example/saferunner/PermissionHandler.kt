package com.example.saferunner

// Android necessities
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// Debug
import android.util.Log

class PermissionHandler(_activity: Activity) {
    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.INTERNET
//        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )
    private val permissionCode = 1
    private var activity = _activity

    fun requirePermissions() {
        while (checkPermissions() == PackageManager.PERMISSION_DENIED) {
            Log.d("Debug", "Requiring permissions")
            ActivityCompat.requestPermissions(activity, permissions, permissionCode)
        }
        Log.d("Debug", "Permissions were satisfied!" + checkPermissions())
    }

    private fun checkPermissions(): Int {
        for (permission in permissions) {
            var isMissingPermission = ContextCompat.checkSelfPermission(activity, permission)

            Log.d("DEBUG", "PERMISSION" + permission + isMissingPermission)
            Log.d("GRANTED", PackageManager.PERMISSION_GRANTED.toString())

            if (isMissingPermission == PackageManager.PERMISSION_DENIED) {
                return PackageManager.PERMISSION_DENIED
            }
        }

        return PackageManager.PERMISSION_GRANTED
    }
}
