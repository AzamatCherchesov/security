package com.a1tt.security.AnalysResults

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.a1tt.security.R
import com.a1tt.security.TargetApplication

class AppAnalysResult : Fragment() {

    lateinit var mTargetApplication: TargetApplication

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

        val view = inflater.inflate(R.layout.app_analys_result, container, false)
        val imageView = view.findViewById<ImageView>(R.id.analysedAppIcon)
        imageView.setImageDrawable(mTargetApplication.icon)
        val appName = view.findViewById<TextView>(R.id.analysedAppName)
        appName.text = mTargetApplication.appName
        return view
    }
}