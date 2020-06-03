package com.example.saferunner

// Usual android necessities
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Debugging
import android.widget.Button


class MainActivity : AppCompatActivity() {
    lateinit var runnerGuard: RunnerGuard
    lateinit var permissionHandler: PermissionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar: androidx.appcompat.widget.Toolbar? = findViewById(R.id.custom_toolbar)
        setSupportActionBar(toolbar)

        permissionHandler = PermissionHandler(this)
        permissionHandler.requirePermissions()

        runnerGuard = RunnerGuard(applicationContext)
        runnerGuard.setStatusCallback { statusText, statusType -> setStatusText(statusText, statusType) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent = when(item.title) {
            getString(R.string.settings_name) -> Intent(this, SettingsActivity::class.java)
            else -> Intent(this, this::class.java)
        }
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }

    fun toggleRunnerGuard(view: View) {
        runnerGuard.toggleActivation()
        updateActivateButtonText()
    }

    private fun updateActivateButtonText() {
        var buttonText = generateCurrentButtonText()
        findViewById<Button>(R.id.toggle_guard_button).text = buttonText
    }

    private fun generateCurrentButtonText(): String = when (runnerGuard.isActive) {
        true -> getString(R.string.deactivate_runner_guard)
        false -> getString(R.string.activate_runner_guard)
    }

    private fun setStatusText(statusText: String, statusType: StatusType) {
        (findViewById<View>(R.id.status_text_view) as TextView).text = statusText

        var color = statusTypeToColor(statusType)
        (findViewById<View>(R.id.status_text_view) as TextView).setTextColor(color)
    }

    private fun statusTypeToColor(statusType: StatusType): Int = when (statusType) {
            StatusType.INFORMATION -> Color.GREEN
            StatusType.WARNING -> Color.YELLOW
            StatusType.ERROR -> Color.RED
    }
}
