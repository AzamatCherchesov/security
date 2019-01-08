package com.a1tt.security.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class SingleURLResultController {
    val liveData = MutableLiveData<ScannedURL>()

    fun getData(): LiveData<ScannedURL> {
        return liveData
    }
}