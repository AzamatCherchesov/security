package com.a1tt.security.AnalysResults

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.ListUpdateCallback
import android.support.v7.util.SortedList
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import com.a1tt.security.MainApplication
import com.a1tt.security.R
import com.a1tt.security.data.ScanedURL

class URLAnalysResult : Fragment() {

    private lateinit var cardsRecycler: RecyclerView
    private lateinit var mAdapter: TargetURLAdapter

    override fun onDetach() {
        super.onDetach()
        mAdapter.unsubscribe()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Analyses result"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = GONE

        val view = inflater.inflate(R.layout.cards_fragment_layout, container, false)
        cardsRecycler = view.findViewById<View>(R.id.cards_recycler) as RecyclerView
        cardsRecycler.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = TargetURLAdapter()
        cardsRecycler.adapter = mAdapter
        return view
    }


    inner class TargetURLAdapter : RecyclerView.Adapter<TargetURLViewHolder>() {
        private val scanedURLs: SortedList<ScanedURL>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetURLViewHolder {
            val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.card_item_view, parent, false)

            return TargetURLViewHolder(view)
        }

        override fun onBindViewHolder(holder: TargetURLViewHolder, position: Int) {
            holder.title.text = scanedURLs[position].scanedURL
            holder.message.text = scanedURLs[position].scanDate
        }

        override fun getItemCount(): Int {
            return scanedURLs.size()
        }

        fun unsubscribe() {
            MainApplication.urlDataManager.unSubscribe(mListUpdateCallback)
        }

        val mListUpdateCallback : ListUpdateCallback = object : ListUpdateCallback {
            override fun onChanged(p0: Int, p1: Int, p2: Any?) {
                activity?.runOnUiThread {
                    this@TargetURLAdapter.notifyItemRangeChanged(p0, p1, p2)
                }
            }

            override fun onMoved(p0: Int, p1: Int) {
                activity?.runOnUiThread {
                    this@TargetURLAdapter.notifyItemMoved(p0, p1)
                }
            }

            override fun onInserted(p0: Int, p1: Int) {
                activity?.runOnUiThread {
                    this@TargetURLAdapter.notifyItemRangeInserted(p0, p1)
                }
            }

            override fun onRemoved(p0: Int, p1: Int) {
                activity?.runOnUiThread {
                    this@TargetURLAdapter.notifyItemRangeRemoved(p0, p1)
                }
            }

        }

        init {
            scanedURLs = MainApplication.urlDataManager.getAllScanedURLs()
            MainApplication.urlDataManager.observe(mListUpdateCallback)
        }
    }

    inner class TargetURLViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<View>(R.id.card_title) as TextView
        var message: TextView = itemView.findViewById<View>(R.id.card_message) as TextView
    }
}
