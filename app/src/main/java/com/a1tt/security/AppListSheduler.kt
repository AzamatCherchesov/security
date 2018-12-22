package com.a1tt.security

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Handler
import com.a1tt.security.TargetAppListFragment.Companion.mTargetApplications

class AppListSheduler(val context: Context, val handler: Handler, val filterStr : String?) : Runnable {

    override fun run() {
        val targetApplications: MutableList<TargetApplication> = mutableListOf<TargetApplication>()
        val packageManager: PackageManager = context.packageManager
        val packages: List<ApplicationInfo> = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)
        var i = 0

        if (filterStr != null) {
            for (pack in packs) {
                if ((pack.applicationInfo.loadLabel(packageManager) as String).contains(filterStr) ||
                        pack.packageName.contains(filterStr)  ){
                    val targetApplication= TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                            packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = if(i%2 == 0) "some info " else null)
                    targetApplications.add(targetApplication)
                    i++
                }
            }
        } else {
            for (pack in packs) {
                val targetApplication= TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                        packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = if(i%2 == 0) "some info " else null)
                targetApplications.add(targetApplication)
                i++
            }
        }
        mTargetApplications = targetApplications
        handler.sendEmptyMessage(1)
    }
}