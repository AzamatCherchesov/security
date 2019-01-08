package com.a1tt.security.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class SingleFileResultController {
    val liveData = MutableLiveData<ScannedFile>()

    fun getData(): LiveData<ScannedFile> {
        return liveData
    }
}