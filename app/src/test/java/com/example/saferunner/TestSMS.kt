package com.example.saferunner

import com.example.saferunner.usecases.MessageSystem


class TestSMS : MessageSystem {
    var sentMessages = HashMap<String, String>()

    override fun sendMassage(message: String, receivers: Array<String>) {
        for (receiver in receivers) sentMessages[receiver] = message
    }
}