package com.a1tt.security.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class SingleFileResultController {
    val liveData = MutableLiveData<ScanedFile>()

    fun getData(): LiveData<ScanedFile> {
        return liveData
    }
}