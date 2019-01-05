package com.a1tt.security.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData



class SingleURLResultController {
    val liveData = MutableLiveData<ScanedURL>()

    fun getData(): LiveData<ScanedURL> {
        return liveData
    }
}