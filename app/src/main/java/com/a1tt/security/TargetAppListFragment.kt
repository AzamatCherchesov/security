package com.a1tt.security

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.a1tt.security.AnalysResults.AppAnalysResult
import com.a1tt.security.Consts.Companion.GET_ALL_APPS
import com.a1tt.security.R.drawable.ic_app_checked_good

class TargetAppListFragment : Fragment() {
    lateinit var targetApplicationRecyclerView: RecyclerView
    lateinit var mAdapter: TargetAppAdapter

    fun filterAppList(p0: String?) {
        if (p0 != null) {
            Thread(AppListSheduler(activity as Context, mHandler, p0)).start()
        } else {
            Thread(AppListSheduler(activity as Context, mHandler, null)).start()
        }
    }

    @SuppressLint("HandlerLeak")
    val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                GET_ALL_APPS -> {
                    val apps: List<TargetApplication> = mTargetApplications
                    mAdapter = TargetAppAdapter(apps)
                    targetApplicationRecyclerView.adapter = mAdapter
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Installed apps"
        val view: View = inflater.inflate(R.layout.target_app_list, container, false)
        targetApplicationRecyclerView = view.findViewById(R.id.target_app_list_recycler_view) as RecyclerView
        targetApplicationRecyclerView.layoutManager = LinearLayoutManager(activity)

        Thread(AppListSheduler(activity as Context, mHandler, null)).start()
        return view
    }

    inner class TargetAppHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(p0: View?) {
            println("A1tt" + application.appName + " clicked!!!!")

            //make all methods private
            //TODO вынести listener - принять его как аргумент
            if (application.result == null) {
                CheckAppFragment().setName(application.appName).show(fragmentManager, "")
            } else {
                MainActivity.router.navigateTo(fragmentFactory = {
                    val appAnalysResult = AppAnalysResult()
                    appAnalysResult.setApplication(application)
//                    appAnalysResult.mTargetApplication = application
                    appAnalysResult
                })
            }
        }

        val appName: TextView
        val packageName: TextView
        val result: TextView
        val iconDrawable: ImageView
        val resultIcon: ImageView

        lateinit var application: TargetApplication

        init {
            appName = itemView.findViewById(R.id.AppName) as TextView
            packageName = itemView.findViewById(R.id.PackageName) as TextView
            iconDrawable = itemView.findViewById(R.id.imageView2) as ImageView
            result = itemView.findViewById(R.id.ResultInfo) as TextView
            resultIcon = itemView.findViewById(R.id.view) as ImageView
            itemView.setOnClickListener(this)
        }

        fun bindTargetApplication(app: TargetApplication) {
            application = app
            appName.text = application.appName
            packageName.text = application.packageName
            result.text = application.result
            if (result.text != null && result.text != "") {
                resultIcon.setImageResource(ic_app_checked_good)
                resultIcon.setColorFilter(Color.GREEN)
            }
            iconDrawable.setImageDrawable(application.icon)
        }
    }

    inner class TargetAppAdapter(apps: List<TargetApplication>) : RecyclerView.Adapter<TargetAppHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TargetAppHolder {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view: View = layoutInflater.inflate(R.layout.card_appinfo_view, p0, false)
            return TargetAppHolder(view)
        }

        override fun getItemCount(): Int {
            return targetApps.size
        }

        override fun onBindViewHolder(p0: TargetAppHolder, p1: Int) {
            val targetApplication: TargetApplication = targetApps[p1]
//            p0.mTextView.text = targetApplication.appName
            p0.bindTargetApplication(targetApplication)
        }

        val targetApps: List<TargetApplication> = apps

    }

    companion object {
        var mTargetApplications: MutableList<TargetApplication> = mutableListOf()
    }
}