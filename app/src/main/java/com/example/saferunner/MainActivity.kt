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
        (findViewById<View>(R.id.status_text_view) as TextView).text = statusText

        var color = statusTypeToColor(statusType)
        (findViewById<View>(R.id.status_text_view) as TextView).setTextColor(color)
    }

    fun statusTypeToColor(statusType: StatusType): Int {
        when (statusType) {
            StatusType.INFORMATION -> return Color.GREEN
            StatusType.WARNING -> return Color.YELLOW
            StatusType.ERROR -> return Color.RED
        }
    }

    fun toggleRunnerGuard(view: View) {
        Log.d("DEBUG", "RAN!")
        runnerGuard.toggleActivation()
    }
}
