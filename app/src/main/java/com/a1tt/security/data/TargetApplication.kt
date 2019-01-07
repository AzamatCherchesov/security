package com.a1tt.security.data

import android.graphics.drawable.Drawable

data class TargetApplication(var appName: String, val packageName: String, val icon: Drawable, var result: String?, val apkFilePath: String)