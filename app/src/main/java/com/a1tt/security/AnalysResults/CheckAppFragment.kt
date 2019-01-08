package com.a1tt.security.AnalysResults

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.a1tt.security.Constants
import com.a1tt.security.MainActivity
import com.a1tt.security.MainApplication
import com.a1tt.security.data.TargetApplication
import com.a1tt.security.shedulers.ScanFileScheduler

class CheckAppFragment : DialogFragment() {
    lateinit var appName: String
    lateinit var apkFilePath: String
    lateinit var application: TargetApplication


    fun setApplication(application: TargetApplication): CheckAppFragment {
        this.appName = application.packageName
        this.apkFilePath = application.apkFilePath
        this.application = application
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity).setTitle("Отправка приложения на анализ")
                .setMessage("Отправить приложение \"$appName\" на анализ?")
                .setPositiveButton("отправить") { p0, p1 ->
                    val firstPair = Pair(Constants.APIKEY_STR, "746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225")
                    val args: MutableList<Pair<String, String>> = mutableListOf(firstPair)

                    Thread(ScanFileScheduler(true, "https://www.virustotal.com/vtapi/v2/file/scan", args, MainActivity.mainHandler, apkFilePath, appName)).start()
                    application.result = "sent for scan"
                    MainApplication.appDataManager.removeApp(application)
                    MainApplication.appDataManager.addApp(application)
                }
                .setNegativeButton("отмена", null)
                .create()
    }
}