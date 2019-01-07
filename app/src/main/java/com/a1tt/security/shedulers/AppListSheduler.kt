package com.a1tt.security.shedulers

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.a1tt.security.MainApplication
import com.a1tt.security.data.TargetApplication

class AppListSheduler(val context: Context, val filterStr: String?) : Runnable {

    override fun run() {
        val packageManager: PackageManager = context.packageManager
        val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)
        var i = 0

        if (filterStr != null) {
            for (pack in packs) {
                if ((pack.applicationInfo.loadLabel(packageManager) as String).contains(filterStr) ||
                        pack.packageName.contains(filterStr)) {
                    val targetApplication = TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                            packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = if (i % 2 == 0) "some info " else null,
                            apkFilePath = pack.applicationInfo.publicSourceDir)
                    MainApplication.appDataManager.addApp(targetApplication)
                    i++
                }
            }
        } else {
            for (pack in packs) {

                val targetApplication = TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                        packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = if (i % 2 == 0) "some info " else null,
                        apkFilePath = pack.applicationInfo.publicSourceDir)
                MainApplication.appDataManager.addApp(targetApplication)
                i++

            }
        }
    }
}