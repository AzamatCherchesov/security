package com.a1tt.security.AnalysResults

import android.arch.lifecycle.LiveData
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
import com.a1tt.security.data.ScanedURL
import com.a1tt.security.shedulers.DBWorker

class SingleURLResult : Fragment() {

    lateinit var myview : View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "URL analyses result"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = View.GONE

        val url = arguments?.getString("url", null)
        MainApplication.dbSheduler.executor.execute(DBWorker("select", mainHandler, null, url))

        myview = inflater.inflate(R.layout.test_layout, container, false)

        val liveData = singleURLResultController.getData()

        liveData.observe(this@SingleURLResult,  Observer<ScanedURL> {
            myview.findViewById<TextView>(R.id.urlView).text = it?.scanedURL
            myview.findViewById<TextView>(R.id.scanDateView).text = it?.scanDate
            myview.findViewById<TextView>(R.id.verboseMsg).text = it?.verboseMsg
            myview.findViewById<TextView>(R.id.number1).text = it?.numberPositives.toString()
            myview.findViewById<TextView>(R.id.number2).text = it?.numberTotal.toString()
        })

//        val view = inflater.inflate(R.layout.single_scaned_url_result, container, false)

//        val cardsRecycler = view.findViewById<View>(R.id.singleURLContentRecycler) as RecyclerView
//        cardsRecycler.layoutManager =
//                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        val mAdapter = SingleURLResultAdapter()
//        cardsRecycler.adapter = mAdapter
        return myview
    }


    inner class SingleURLResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    }

    inner class SingleURLResultAdapter : RecyclerView.Adapter<SingleURLResultHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SingleURLResultHolder {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view: View = layoutInflater.inflate(R.layout.card_appinfo_view, p0, false)
            return SingleURLResultHolder(view)
        }

        override fun getItemCount(): Int {
            return 1
        }

        override fun onBindViewHolder(p0: SingleURLResultHolder, p1: Int) {
        }


    }

}