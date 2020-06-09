package com.example.saferunner

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.EditTextPreference.OnBindEditTextListener
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            useNumericalInput(getString(R.string.preferences_notification_message_key))
            useNumericalInput(getString(R.string.preferences_speed_threshold_key))
        }
            
        private fun useNumericalInput(key: String) {
            val editTextPreference =
                preferenceManager.findPreference<EditTextPreference>(key)
            editTextPreference!!.onBindEditTextListener =
                OnBindEditTextListener { editText ->
                    editText.inputType = InputType.TYPE_CLASS_PHONE
                }
        }
    }
}