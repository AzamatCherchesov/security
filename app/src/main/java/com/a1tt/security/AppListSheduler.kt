package com.a1tt.security

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class AppListSheduler(val context: Context, val filterStr: String?) : Runnable {

    override fun run() {
//        val targetApplications: MutableList<TargetApplication> = mutableListOf()
        val packageManager: PackageManager = context.packageManager
        val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)
        var i = 0

        if (filterStr != null) {
            for (pack in packs) {
                if ((pack.applicationInfo.loadLabel(packageManager) as String).contains(filterStr) ||
                        pack.packageName.contains(filterStr)) {
                    val targetApplication = TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                            packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = if (i % 2 == 0) "some info " else null)
                    MainApplication.appDataManager.addApp(targetApplication)
                    i++
                }
            }
        } else {
            for (pack in packs) {
                val targetApplication = TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                        packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = if (i % 2 == 0) "some info " else null)
                MainApplication.appDataManager.addApp(targetApplication)
                i++

            }
        }



//        mInstalledApplications = targetApplications
//        MainApplication.mListener.objectCreated()
//        handler.sendEmptyMessage(1)
    }
}