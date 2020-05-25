package com.example.saferunner

// Android necessities
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// Debug
import android.util.Log


class PermissionHandler(activity: Activity) {
    // TODO: Pass permissions needed to class
    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.SEND_SMS,
        android.Manifest.permission.WAKE_LOCK
    )
    private var gotPermissions: Boolean = false
    private val permissionCode = 1
    private var activity = activity

    fun requirePermissions() {
        if (!gotPermissions) {
            while (checkPermissions() == PackageManager.PERMISSION_DENIED) {
                Log.d("Debug", "Requiring permissions")
                ActivityCompat.requestPermissions(activity, permissions, permissionCode)
            }
            gotPermissions = true
            Log.d("Debug", "Permissions were satisfied!" + checkPermissions())
        }
    }

    private fun checkPermissions(): Int {
        for (permission in permissions) {
            var isMissingPermission = ContextCompat.checkSelfPermission(activity, permission)

            Log.d("DEBUG", "Permission $permission $isMissingPermission")
            Log.d("GRANTED", PackageManager.PERMISSION_GRANTED.toString())

            if (isMissingPermission == PackageManager.PERMISSION_DENIED) {
                return PackageManager.PERMISSION_DENIED
            }
        }

        return PackageManager.PERMISSION_GRANTED
    }
}
