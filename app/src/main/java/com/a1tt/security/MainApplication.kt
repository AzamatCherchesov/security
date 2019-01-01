package com.a1tt.security

import android.app.Application
import android.os.CountDownTimer
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MainApplication
            private set
        var countDownTimer: CountDownTimer? = null
        var timerCounter: Long = 3000

        val executor: Executor = Executors.newSingleThreadExecutor()
        val appDataManager = AppDataManager()
        val urlDataManager = URLDataManager()

    }
}