package com.a1tt.security.AnalysResults

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.ListUpdateCallback
import android.support.v7.util.SortedList
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.a1tt.security.MainActivity
import com.a1tt.security.MainActivity.Companion.mainHandler
import com.a1tt.security.MainApplication
import com.a1tt.security.MainApplication.Companion.singleURLResultController
import com.a1tt.security.R
import com.a1tt.security.data.ScanedURL
import com.a1tt.security.data.ServicesResult
import com.a1tt.security.data.TargetApplication
import com.a1tt.security.shedulers.DBWorker

class SingleURLResult : Fragment() {

    lateinit var myview : View
    lateinit var targetApplicationRecyclerView: RecyclerView
    lateinit var mAdapter: TargetAppAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "URL analyses result"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = View.GONE

        val url = arguments?.getString("url", null)
        MainApplication.dbSheduler.executor.execute(DBWorker(activity!!.applicationContext, "select", mainHandler, null, url))

        myview = inflater.inflate(R.layout.test_layout, container, false)

        val liveData = singleURLResultController.getData()

        liveData.observe(this@SingleURLResult,  Observer<ScanedURL> {
            myview.findViewById<TextView>(R.id.urlView).text = it?.scanedURL
            myview.findViewById<TextView>(R.id.scanDateView).text = it?.scanDate
            myview.findViewById<TextView>(R.id.verboseMsg).text = it?.verboseMsg
            myview.findViewById<TextView>(R.id.number1).text = it?.numberPositives.toString()
            myview.findViewById<TextView>(R.id.number2).text = it?.numberTotal.toString()
        })

        targetApplicationRecyclerView = myview.findViewById(R.id.scans_recycler) as RecyclerView
        targetApplicationRecyclerView.layoutManager = LinearLayoutManager(activity)

        mAdapter = TargetAppAdapter()
        targetApplicationRecyclerView.adapter = mAdapter
        return myview
    }

    inner class TargetAppHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class TargetAppAdapter : RecyclerView.Adapter<TargetAppHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TargetAppHolder {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view: View = layoutInflater.inflate(R.layout.card_service_scan, p0, false)
            return TargetAppHolder(view)
        }

        override fun getItemCount(): Int {
            return 25
//            return targetApps.size
        }

        override fun onBindViewHolder(p0: TargetAppHolder, p1: Int) {
        }

        lateinit var targetApps: MutableList<ServicesResult>



        init {
//            targetApps = MutableList<ServicesResult>()
//            targetApps = MainApplication.appDataManager.getAllInstalledApp()
        }
    }

}