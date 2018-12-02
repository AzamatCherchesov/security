package com.a1tt.security

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class TargetAppLab (context: Context){
    var targetApplications: MutableList<TargetApplication> = mutableListOf<TargetApplication>()

    init {
        val packageManager: PackageManager = context.packageManager
        val packages: List<ApplicationInfo> = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val packs: List<PackageInfo> = packageManager.getInstalledPackages(0)
        var i = 0
        for (pack in packs) {
            var targetApplication: TargetApplication = TargetApplication(appName = pack.applicationInfo.loadLabel(packageManager) as String,
                    packageName = pack.packageName, icon = pack.applicationInfo.loadIcon(packageManager), result = if(i%2 == 0) "some info " else null)
            this.targetApplications.add(targetApplication)
            i++
        }
    }
}