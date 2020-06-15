package com.example.saferunner

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import androidx.preference.EditTextPreference.OnBindEditTextListener


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
        private var receivers: MutableSet<String>?
            get() = preferenceManager.sharedPreferences.getStringSet("s", null) // TODO: replace "s"
            set(value) {
                var preferenceEditor: SharedPreferences.Editor = preferenceManager.sharedPreferences.edit()
                preferenceEditor.putStringSet("s", value)
                preferenceEditor.apply()
            }
        private var phoneNumberEditTextKeys: MutableSet<String>?
            get() = preferenceManager.sharedPreferences.getStringSet("t", null) // TODO: replace "t" & Reuse get/set!
            set(value) {
                var preferenceEditor: SharedPreferences.Editor = preferenceManager.sharedPreferences.edit()
                preferenceEditor.putStringSet("t", value)
                preferenceEditor.apply()
            }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            configureAddPhoneNumberPreference()
            configureRemovePhoneNumberPreference()

            useNumericalInput(getString(R.string.preferences_notification_message_key))
            useNumericalInput(getString(R.string.preferences_speed_threshold_key))

            loadExtraPhoneNumberPreferences()
        }


        private fun configureAddPhoneNumberPreference() {
            val button: Preference? =
                findPreference(getString(R.string.preference_extra_add_phone_number_key))
            button!!.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    createNewPhoneNumberPreference()
                    true
                }
        }

        private fun configureRemovePhoneNumberPreference() {
            val button: Preference? =
                findPreference(getString(R.string.preference_extra_remove_phone_number_key))
            button!!.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    removeLastPhoneNumberPreference()
                    true
                }
        }

        private fun removeLastPhoneNumberPreference() {
            if (!phoneNumberEditTextKeys.isNullOrEmpty()) removePhoneNumberEditTextPreference(phoneNumberEditTextKeys!!.toSortedSet().last())
            Log.d("Keys", phoneNumberEditTextKeys.toString())

        }

        private fun removePhoneNumberEditTextPreference(key: String) {
            var category: PreferenceCategory? = preferenceScreen.findPreference(getString(R.string.preference_category_phone_number))

            var editTextPreference: EditTextPreference? = findPreference(key)
            category?.removePreference(editTextPreference)

            phoneNumberEditTextKeys = removeValueFromMutableSet(phoneNumberEditTextKeys, key)
        }

        private fun useNumericalInput(key: String) {
            val editTextPreference =
                preferenceManager.findPreference<EditTextPreference>(key)
            editTextPreference!!.onBindEditTextListener =
                OnBindEditTextListener { editText ->
                    editText.inputType = InputType.TYPE_CLASS_PHONE
                }
        }

        private fun loadExtraPhoneNumberPreferences() {
            if (!phoneNumberEditTextKeys.isNullOrEmpty()) {
                for (key in phoneNumberEditTextKeys!!.toSortedSet()!!.toTypedArray()) {
                    addPhoneNumberEditTextPreference(key)
                }
            }
        }

        private fun addPhoneNumberEditTextPreference(key: String) {
            var category: PreferenceCategory? = preferenceScreen.findPreference(getString(R.string.preference_category_phone_number))

            var editTextPreference: EditTextPreference = EditTextPreference(preferenceScreen.context)
            editTextPreference.title = getString(R.string.receiver_title)
            editTextPreference.summary = getString(R.string.preference_receiver_summary)
            editTextPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference, any: Any ->
                fun onPreferenceChange(preference: Preference, value: String) {
                    saveReceivers()
                }
                true
            }

            phoneNumberEditTextKeys = appendValueToMutableSet(phoneNumberEditTextKeys, key)

            editTextPreference.key = key
            category?.addPreference(editTextPreference)

            useNumericalInput(key)

            Log.d("Keys", phoneNumberEditTextKeys.toString())
        }

        private fun appendValueToMutableSet(mutableSet: MutableSet<String>?, value: String): MutableSet<String> {
            var newMutableSet = arrayListOf<String>()
            if (!mutableSet.isNullOrEmpty()) {
                newMutableSet.addAll(mutableSet)
            }
            newMutableSet.add(value)

            return newMutableSet.toMutableSet()
        }

        private fun removeValueFromMutableSet(mutableSet: MutableSet<String>?, value: String): MutableSet<String> {
            var newMutableSet = arrayListOf<String>()
            if (!mutableSet.isNullOrEmpty()) {
                for (oldValue in mutableSet!!) {
                    if (oldValue == value) continue
                    newMutableSet.add(oldValue)
                }
            }

            return newMutableSet.toMutableSet()
        }

        private fun createNewPhoneNumberPreference()  {
            var key = generateNewPhoneNumberEditTextKey()
            addPhoneNumberEditTextPreference(key)
        }

        private fun saveReceivers() {
            receivers = getPhoneNumbers()
            Log.d("Phone Numbers", receivers.toString())
        }

        private fun getPhoneNumbers(): MutableSet<String>? {
            var phoneNumbers = arrayListOf<String>()

            return if (!phoneNumberEditTextKeys.isNullOrEmpty()) {
                for (editPreferenceKey in phoneNumberEditTextKeys!!) {
                    var editTextPreference: EditTextPreference? = findPreference(editPreferenceKey)
                    if (editTextPreference?.text != "")
                    phoneNumbers.add(editTextPreference!!.text)
                }
                phoneNumbers.toMutableSet()
            }
            else null
        }

        private fun generateNewPhoneNumberEditTextKey(): String {
            var baseKey = getString(R.string.preference_extra_phone_number_key)

            if (phoneNumberEditTextKeys.isNullOrEmpty()) return baseKey

            while (true) {
                var key = phoneNumberEditTextKeys?.toSortedSet()?.last() + randomKeyDigit()
                if (!phoneNumberEditTextKeys?.toTypedArray()!!.contains(key)) return key
            }
        }

        private fun randomKeyDigit(): Int {
            return (0..9).random()
        }
    }
}