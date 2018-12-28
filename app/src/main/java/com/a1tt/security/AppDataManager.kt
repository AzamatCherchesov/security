package com.a1tt.security

import android.support.v7.util.SortedList

class AppDataManager : Observable() {

    private var mInstalledApplications: SortedList<TargetApplication> = SortedList<TargetApplication>(TargetApplication::class.java, object : SortedList.Callback<TargetApplication>() {
        override fun areItemsTheSame(p0: TargetApplication?, p1: TargetApplication?): Boolean {
            return p0?.packageName.equals(p1?.packageName)
        }

        override fun onMoved(p0: Int, p1: Int) {
            this@AppDataManager.onMoved(p0, p1)
        }

        override fun onChanged(p0: Int, p1: Int) {
           this@AppDataManager.onChanged(p0, p1, null)
        }

        override fun onInserted(p0: Int, p1: Int) {
            this@AppDataManager.onInserted(p0, p1)
        }

        override fun onRemoved(p0: Int, p1: Int) {
            this@AppDataManager.onRemoved(p0, p1)
        }

        override fun compare(p0: TargetApplication?, p1: TargetApplication?): Int {
            return p0?.packageName?.compareTo(p1?.packageName!!)!!
        }

        override fun areContentsTheSame(p0: TargetApplication?, p1: TargetApplication?): Boolean {
            return p0?.equals(p1)!!
        }

    })

    fun addApp (application: TargetApplication) {
        mInstalledApplications.add(application)

    }

    fun removeApp (application: TargetApplication) {
        mInstalledApplications.remove(application)
    }

    fun getAllInstalledApp (): SortedList<TargetApplication> {
        return mInstalledApplications
    }
}