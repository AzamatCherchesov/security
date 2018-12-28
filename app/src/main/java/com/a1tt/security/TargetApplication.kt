package com.a1tt.security

import android.graphics.drawable.Drawable
import java.io.Serializable

data class TargetApplication(var appName: String, val packageName: String, val icon: Drawable, val result: String?)