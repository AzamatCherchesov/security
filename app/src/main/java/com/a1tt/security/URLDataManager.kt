package com.a1tt.security

import android.support.v7.util.SortedList
import com.a1tt.security.data.ScannedURL

class URLDataManager : Observable() {
    private var mScannedURLs: SortedList<ScannedURL> = SortedList<ScannedURL>(ScannedURL::class.java, object : SortedList.Callback<ScannedURL>() {
        override fun areItemsTheSame(p0: ScannedURL?, p1: ScannedURL?): Boolean {
            return p0?.scannedURL.equals(p1?.scannedURL)
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

        override fun compare(p0: ScannedURL?, p1: ScannedURL?): Int {
            return p0?.scannedURL?.compareTo(p1?.scannedURL!!)!!
        }

        override fun areContentsTheSame(p0: ScannedURL?, p1: ScannedURL?): Boolean {
            return p0?.equals(p1)!!
        }

    })

    fun addURL(scannedURL: ScannedURL) {
        mScannedURLs.add(scannedURL)

    }

    fun removeURL(scannedURL: ScannedURL) {
        mScannedURLs.remove(scannedURL)
    }

    fun getAllScannedURLs(): SortedList<ScannedURL> {
        return mScannedURLs
    }

}