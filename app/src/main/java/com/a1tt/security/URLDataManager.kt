package com.a1tt.security

import android.support.v7.util.SortedList
import com.a1tt.security.data.ScanedURL

class URLDataManager : Observable() {
    private var mScanedURLs: SortedList<ScanedURL> = SortedList<ScanedURL>(ScanedURL::class.java, object : SortedList.Callback<ScanedURL>() {
        override fun areItemsTheSame(p0: ScanedURL?, p1: ScanedURL?): Boolean {
            return p0?.scanedURL.equals(p1?.scanedURL)
        }

        override fun onMoved(p0: Int, p1: Int) {
            this@URLDataManager.onMoved(p0, p1)
        }

        override fun onChanged(p0: Int, p1: Int) {
            this@URLDataManager.onChanged(p0, p1, null)
        }

        override fun onInserted(p0: Int, p1: Int) {
            this@URLDataManager.onInserted(p0, p1)
        }

        override fun onRemoved(p0: Int, p1: Int) {
            this@URLDataManager.onRemoved(p0, p1)
        }

        override fun compare(p0: ScanedURL?, p1: ScanedURL?): Int {
            return p0?.scanedURL?.compareTo(p1?.scanedURL!!)!!
        }

        override fun areContentsTheSame(p0: ScanedURL?, p1: ScanedURL?): Boolean {
            return p0?.equals(p1)!!
        }

    })

    fun addApp (application: ScanedURL) {
        mScanedURLs.add(application)

    }

    fun removeApp (application: ScanedURL) {
        mScanedURLs.remove(application)
    }

    fun getAllInstalledApp (): SortedList<ScanedURL> {
        return mScanedURLs
    }

}