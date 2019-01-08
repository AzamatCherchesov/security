package com.a1tt.security.shedulers

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.a1tt.security.MainApplication
import com.a1tt.security.data.TargetApplication

class AppListScheduler(val context: Context, private val pattern: String?) : Runnable {

    override fun run() {
        val packageManager: PackageManager = context.packageManager
        val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)
        var i = 0

        if (pattern != null) {
            for (pack in packs) {
                if ((pack.applicationInfo.loadLabel(packageManager) as String).contains(pattern) ||
                        pack.packageName.contains(pattern)) {
//                    result = if (i % 2 == 0) "some info " else null
                    val targetApplication = TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                            packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = null,
                            apkFilePath = pack.applicationInfo.publicSourceDir)
                    MainApplication.appDataManager.addApp(targetApplication)
                    i++
                }
            }
        } else {
            for (pack in packs) {
//                result = if (i % 2 == 0) "some info " else null
                val targetApplication = TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                        packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = null,
                        apkFilePath = pack.applicationInfo.publicSourceDir)
                MainApplication.appDataManager.addApp(targetApplication)
                i++

            }
        }
    }
}