package com.a1tt.security

import android.support.v7.util.ListUpdateCallback

abstract class Observable : ListUpdateCallback {
    private val mList = mutableListOf<ListUpdateCallback>()

    fun observe(observer: ListUpdateCallback) {
        mList.add(observer)
    }

    fun unSubscribe(observer: ListUpdateCallback) {
        mList.remove(observer)
    }

    override fun onChanged(p0: Int, p1: Int, p2: Any?) {
        for (elem in mList) {
            elem.onChanged(p0, p1, p2)
        }
    }

    override fun onMoved(p0: Int, p1: Int) {
        for (elem in mList) {
            elem.onMoved(p0, p1)
        }
    }

    override fun onInserted(p0: Int, p1: Int) {
        for (elem in mList) {
            elem.onInserted(p0, p1)
        }
    }

    override fun onRemoved(p0: Int, p1: Int) {
        for (elem in mList) {
            elem.onRemoved(p0, p1)
        }
    }

    //    protected fun notifyCreated (t: T) {
//        for (elem : ListUpdateCallback in mList) {
//            elem.objectCreated(t)
//        }
//    }
//
//    protected fun notifyRemove (t: T) {
//        for (elem : ListUpdateCallback in mList) {
//           elem.objectRemoved(t)
//        }
//    }
//
//
//    protected fun notifyModified(t: T) {
//        for (elem : ListUpdateCallback in mList) {
//            elem.objectModified(t)
//        }
//    }
}