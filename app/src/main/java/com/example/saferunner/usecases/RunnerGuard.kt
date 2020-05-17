package com.example.saferunner.usecases

interface RunnerGuard {
    var isActive: Boolean

    fun activate()
    fun deactivate()
    fun toggleActivation()

    fun checkRunnability(): Boolean

    fun checkIfAlive()
}