package com.a1tt.security.AnalysResults

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a1tt.security.R

class SingleURLResult : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "URL analyses result"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = View.GONE

        val view = inflater.inflate(R.layout.single_scaned_url_result, container, false)
        val cardsRecycler = view.findViewById<View>(R.id.singleURLContentRecycler) as RecyclerView
        cardsRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        return view
    }

}