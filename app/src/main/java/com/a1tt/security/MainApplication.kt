package com.a1tt.security

import android.app.Application
import android.os.CountDownTimer
import com.a1tt.security.data.SingleFileResultController
import com.a1tt.security.data.SingleURLResultController
import com.a1tt.security.shedulers.DBSheduler
import java.util.concurrent.Executors

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        dbSheduler = DBSheduler(Executors.newSingleThreadExecutor(), applicationContext)
    }

    companion object {
        lateinit var instance: MainApplication
            private set
        var countDownTimer: CountDownTimer? = null
        var timerCounter: Long = 3000

        lateinit var dbSheduler: DBSheduler
        val appDataManager = InstalledAppsDataManager()
        val urlDataManager = URLDataManager()
        val singleURLResultController = SingleURLResultController()
        val singleFileResultController = SingleFileResultController()

    }
}