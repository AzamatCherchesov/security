package com.a1tt.security.AnalysResults

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.a1tt.security.MainActivity
import com.a1tt.security.MainApplication
import com.a1tt.security.R
import com.a1tt.security.data.FileScanServicesResult
import com.a1tt.security.data.ScanedFile
import com.a1tt.security.data.TargetApplication
import com.a1tt.security.shedulers.DBFileWorker
import com.a1tt.security.shedulers.DBURLWorker

class SingleAppAnalysResult : Fragment() {

    lateinit var mTargetApplication: TargetApplication
    lateinit var myview : View
    lateinit var targetApplicationRecyclerView: RecyclerView

    fun setApplication(targetApplication: TargetApplication) {
        mTargetApplication = targetApplication
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Analyses result"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = GONE

        myview = inflater.inflate(R.layout.single_app_analys_result, container, false)
        val imageView = myview.findViewById<ImageView>(R.id.analysedAppIcon)
        imageView.setImageDrawable(mTargetApplication.icon)

        val liveData = MainApplication.singleFileResultController.getData()

        liveData.observe(this@SingleAppAnalysResult,  Observer<ScanedFile> {
            myview.findViewById<TextView>(R.id.fileView).text = it?.scanedFile
            myview.findViewById<TextView>(R.id.scanDateView).text = it?.scanDate
            myview.findViewById<TextView>(R.id.verboseMsg).text = it?.verboseMsg
            myview.findViewById<TextView>(R.id.number1).text = it?.numberPositives.toString()
            myview.findViewById<TextView>(R.id.number2).text = it?.numberTotal.toString()
        })

        targetApplicationRecyclerView = myview.findViewById(R.id.scans_recycler) as RecyclerView
        targetApplicationRecyclerView.layoutManager = LinearLayoutManager(activity)

        mAdapter = TargetAppAdapter()
        targetApplicationRecyclerView.adapter = mAdapter

        MainApplication.dbSheduler.executor.execute(DBFileWorker(activity!!.applicationContext, "select", MainActivity.mainHandler, null, mTargetApplication.appName))
        return myview
    }

    inner class TargetAppHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val service_name: TextView = itemView.findViewById<View>(R.id.service_name) as TextView
        val service_detected: TextView = itemView.findViewById<View>(R.id.service_detected) as TextView
        val service_version: TextView = itemView.findViewById<View>(R.id.service_version) as TextView
        val service_result: TextView = itemView.findViewById<View>(R.id.service_result) as TextView
        val service_update: TextView = itemView.findViewById<View>(R.id.service_update) as TextView
    }

    inner class TargetAppAdapter : RecyclerView.Adapter<TargetAppHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TargetAppHolder {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view: View = layoutInflater.inflate(R.layout.card_service_file_scan, p0, false)
            return TargetAppHolder(view)
        }

        override fun getItemCount(): Int {
//            return 20
            return scanServicesList.size
        }



        override fun onBindViewHolder(holder: TargetAppHolder, position: Int) {
            holder.service_name.text = scanServicesList[position].serviceName
            holder.service_detected.text = scanServicesList[position].detected.toString()
            holder.service_result.text = scanServicesList[position].result
            holder.service_update.text = scanServicesList[position].update
            holder.service_version.text = scanServicesList[position].version
        }
    }

    companion object {
        fun addResultsAppServices(items: MutableList<FileScanServicesResult>) {
            scanServicesList = items
            mAdapter.notifyDataSetChanged()
        }
        lateinit var mAdapter: TargetAppAdapter
        var scanServicesList: MutableList<FileScanServicesResult> = mutableListOf<FileScanServicesResult>()
    }

}