package com.a1tt.security

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class TargetAppListFragment : Fragment() {
    lateinit var targetApplicationRecyclerView: RecyclerView
    lateinit var mAdapter: TargetAppAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.target_app_list, container, false)
        targetApplicationRecyclerView = view.findViewById(R.id.target_app_list_recycler_view) as RecyclerView
        targetApplicationRecyclerView.layoutManager = LinearLayoutManager(activity)

        updateUi()

        return view
    }

    fun updateUi() {
        //TODO new thread
        val appsLab = TargetAppLab(activity as Context)
        val apps: List<TargetApplication> = appsLab.targetApplications
        mAdapter = TargetAppAdapter(apps)
        targetApplicationRecyclerView.adapter = mAdapter
    }

    inner class TargetAppHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {
        override fun onClick(p0: View?) {
            println("A1tt"  + application.appName + " clicked!!!!")

            //make all methods private
            //TODO вынести listener - принять его как аргумент
            if(application.result == null) {
                CheckAppFragment().setName(application.appName).show(fragmentManager, "")
                //TODO:: DIALOG
            }
            else {
                //TODO::
            }
        }

        val appName : TextView
        val packageName : TextView
        val result : TextView
        val iconDrawable : ImageView

        lateinit var application: TargetApplication
        init{
            appName = itemView.findViewById(R.id.AppName) as TextView
            packageName = itemView.findViewById(R.id.PackageName) as TextView
            iconDrawable = itemView.findViewById(R.id.imageView2) as ImageView
            result = itemView.findViewById(R.id.ResultInfo) as TextView
            itemView.setOnClickListener(this)
        }

        fun bindTargetApplication( app: TargetApplication) {
            application = app
            appName.text = application.appName
            packageName.text = application.packageName
            result.text = application.result
            iconDrawable.setImageDrawable(application.icon)
            //TODO::
        }
    }

    inner class TargetAppAdapter(apps: List<TargetApplication>) : RecyclerView.Adapter<TargetAppHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TargetAppHolder {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view: View = layoutInflater.inflate(R.layout.target_app_fragment, p0, false)
            return TargetAppHolder(view)
        }

        override fun getItemCount(): Int {
            return targetApps.size
        }

        override fun onBindViewHolder(p0: TargetAppHolder, p1: Int) {
            val targetApplication: TargetApplication = targetApps.get(p1)
//            p0.mTextView.text = targetApplication.appName
            p0.bindTargetApplication(targetApplication)
        }

        val targetApps : List<TargetApplication>

        init{
            targetApps = apps
        }
    }
}