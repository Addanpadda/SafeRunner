package com.example.saferunner

import android.telephony.SmsManager
import com.example.saferunner.usecases.NotificationSystem

class SMS : NotificationSystem {
    var smsManager: SmsManager = SmsManager.getDefault()

    override fun sendMassage(message: String, receivers: Array<String>) {
        for (receiver in receivers) {
            smsManager.sendTextMessage(receiver, null, message, null, null)
        }
    }
}