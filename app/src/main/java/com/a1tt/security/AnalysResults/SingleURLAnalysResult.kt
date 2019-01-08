package com.a1tt.security.AnalysResults

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.a1tt.security.MainActivity.Companion.mainHandler
import com.a1tt.security.MainApplication
import com.a1tt.security.MainApplication.Companion.singleURLResultController
import com.a1tt.security.R
import com.a1tt.security.data.ScannedURL
import com.a1tt.security.data.URLScanServicesResult
import com.a1tt.security.shedulers.DBURLWorker

class SingleURLAnalysResult : Fragment() {

    lateinit var mAnalysView: View
    lateinit var mRecyclerView: RecyclerView


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "URL analyses result"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = View.GONE

        val url = arguments?.getString("url", null)
        MainApplication.dbScheduler.executor.execute(DBURLWorker(activity!!.applicationContext, "select", mainHandler, null, url))
        mAnalysView = inflater.inflate(R.layout.single_url_analys_result, container, false)

        val liveData = singleURLResultController.getData()
        liveData.observe(this@SingleURLAnalysResult, Observer<ScannedURL> {
            mAnalysView.findViewById<TextView>(R.id.urlView).text = it?.scannedURL
            mAnalysView.findViewById<TextView>(R.id.scanDateView).text = it?.scanDate
            mAnalysView.findViewById<TextView>(R.id.verboseMsg).text = it?.verboseMsg
            mAnalysView.findViewById<TextView>(R.id.number1).text = it?.numberPositives.toString()
            mAnalysView.findViewById<TextView>(R.id.number2).text = it?.numberTotal.toString()
        })

        mRecyclerView = mAnalysView.findViewById(R.id.scans_recycler) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        mAdapter = TargetURLAdapter()
        mRecyclerView.adapter = mAdapter
        return mAnalysView
    }

    inner class TargetURLHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val service_name: TextView = itemView.findViewById<View>(R.id.service_name) as TextView
        val service_detected: TextView = itemView.findViewById<View>(R.id.service_detected) as TextView
        val service_result: TextView = itemView.findViewById<View>(R.id.service_result) as TextView
        val service_detail: TextView = itemView.findViewById<View>(R.id.service_detail) as TextView
    }

    inner class TargetURLAdapter : RecyclerView.Adapter<TargetURLHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TargetURLHolder {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view: View = layoutInflater.inflate(R.layout.card_service_url_scan, p0, false)
            return TargetURLHolder(view)
        }

        override fun getItemCount(): Int {
            return scanServicesList.size
        }

        override fun onBindViewHolder(holder: TargetURLHolder, position: Int) {
            holder.service_name.text = scanServicesList[position].serviceName
            holder.service_detected.text = scanServicesList[position].detected.toString()
            holder.service_result.text = scanServicesList[position].result
            holder.service_detail.text = scanServicesList[position].detail
        }
    }

    companion object {
        fun addResultsServices(items: MutableList<URLScanServicesResult>) {
            scanServicesList = items
            mAdapter.notifyDataSetChanged()
        }

        lateinit var mAdapter: TargetURLAdapter
        var scanServicesList: MutableList<URLScanServicesResult> = mutableListOf<URLScanServicesResult>()
    }

}