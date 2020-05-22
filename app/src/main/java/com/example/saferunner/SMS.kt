package com.example.saferunner

// Android necessities
import android.telephony.SmsManager
import android.util.Log
import com.example.saferunner.usecases.MessageSystem
import java.lang.Exception

class SMS : MessageSystem {
    var smsManager: SmsManager = SmsManager.getDefault()

    override fun sendMassage(message: String, receivers: Array<String>) {
        try {
            for (receiver in receivers) {
                smsManager.sendTextMessage(receiver, null, message, null, null)
            }
        } catch (e: Exception) {
            Log.d("SMS", "Error sending SMS")
        }
    }
}