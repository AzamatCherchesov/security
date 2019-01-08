package com.a1tt.security.AnalysResults

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
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.a1tt.security.MainActivity
import com.a1tt.security.MainApplication
import com.a1tt.security.R
import com.a1tt.security.R.drawable.*
import com.a1tt.security.data.TargetApplication

class TargetAppListFragment : Fragment() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: TargetAppAdapter

    override fun onDetach() {
        super.onDetach()
        mAdapter.unsubscribe()
    }

    fun filterAppList(p0: String?) {
//        if (p0 != null) {
//            Thread(AppListSheduler(activity as Context, mHandler, p0)).start()
//        } else {
//            Thread(AppListSheduler(activity as Context, mHandler, null)).start()
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Installed apps"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = VISIBLE

        val view: View = inflater.inflate(R.layout.target_app_list, container, false)
        mRecyclerView = view.findViewById(R.id.target_app_list_recycler_view) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        mAdapter = TargetAppAdapter()
        mRecyclerView.adapter = mAdapter
        return view
    }

    inner class TargetAppHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(p0: View?) {
            if (application.result == null) {
                CheckAppFragment().setApplication(application).show(fragmentManager, "")
            } else {
                MainActivity.router.navigateTo(fragmentFactory = {
                    val appAnalysResult = SingleAppAnalysResult()
                    appAnalysResult.setApplication(application)
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
            resultIcon.setImageResource(ic_app_unchecked)
            resultIcon.setColorFilter(Color.BLACK)
            iconDrawable.setImageDrawable(application.icon)
            if (result.text.equals("clean")) {
                resultIcon.setImageResource(ic_app_checked_good)
                resultIcon.setColorFilter(Color.GREEN)
            }
            if (result.text.equals("bad")) {
                resultIcon.setImageResource(ic_app_checked_virus)
                resultIcon.setColorFilter(Color.RED)
            }
        }
    }

    inner class TargetAppAdapter : RecyclerView.Adapter<TargetAppHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TargetAppHolder {
            val layoutInflater: LayoutInflater = LayoutInflater.from(activity)
            val view: View = layoutInflater.inflate(R.layout.card_appinfo_view, p0, false)
            return TargetAppHolder(view)
        }

        override fun getItemCount(): Int {
            return targetApps.size()
        }

        override fun onBindViewHolder(p0: TargetAppHolder, p1: Int) {
            val targetApplication: TargetApplication = targetApps[p1]
            p0.bindTargetApplication(targetApplication)
        }

        var targetApps: SortedList<TargetApplication>

        fun unsubscribe() {
            MainApplication.appDataManager.unSubscribe(mListUpdateCallback)
        }

        val mListUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {
            override fun onChanged(p0: Int, p1: Int, p2: Any?) {
                activity?.runOnUiThread {
                    this@TargetAppAdapter.notifyItemRangeChanged(p0, p1, p2)
                }
            }

            override fun onMoved(p0: Int, p1: Int) {
                activity?.runOnUiThread {
                    this@TargetAppAdapter.notifyItemMoved(p0, p1)
                }
            }

            override fun onInserted(p0: Int, p1: Int) {
                activity?.runOnUiThread {
                    this@TargetAppAdapter.notifyItemRangeInserted(p0, p1)
                }
            }

            override fun onRemoved(p0: Int, p1: Int) {
                activity?.runOnUiThread {
                    this@TargetAppAdapter.notifyItemRangeRemoved(p0, p1)
                }
            }

        }

        init {
            targetApps = MainApplication.appDataManager.getAllInstalledApp()
            MainApplication.appDataManager.observe(mListUpdateCallback)
        }
    }
}