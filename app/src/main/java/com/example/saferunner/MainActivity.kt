package com.example.saferunner

// Usual android necessities
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Debugging
import android.util.Log


class MainActivity : AppCompatActivity() {
    lateinit var runnerGuard: RunnerGuard
    lateinit var permissionHandler: PermissionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionHandler = PermissionHandler(this)
        permissionHandler.requirePermissions()

        runnerGuard = RunnerGuard(applicationContext)
        runnerGuard.setStatusCallback { statusText, statusType -> setStatusText(statusText, statusType) }
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
