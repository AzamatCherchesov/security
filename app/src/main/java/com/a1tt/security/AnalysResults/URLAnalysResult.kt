package com.a1tt.security.AnalysResults

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import com.a1tt.security.R
import com.a1tt.security.data.ScanedURL
import java.util.ArrayList

class URLAnalysResult : Fragment() {

    private lateinit var cardsRecycler: RecyclerView

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
        val adapter = TargetURLAdapter()
        cardsRecycler.adapter = adapter
        adapter.addCards(cards)
        adapter.notifyDataSetChanged()
        return view
    }

    companion object {
        val cards = ArrayList<ScanedURL>()
    }

    inner class TargetURLAdapter : RecyclerView.Adapter<TargetURLViewHolder>(), View.OnClickListener {
        override fun onClick(v: View?) {
            Log.e("A1tt", "clicked now")
        }

        private val cards: MutableList<ScanedURL>

        init {
            cards = ArrayList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetURLViewHolder {
            val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.card_item_view, parent, false)
            return TargetURLViewHolder(view)
        }

        override fun onBindViewHolder(holder: TargetURLViewHolder, position: Int) {
//        val (title, message) = cards[position]
            holder.title.text = cards[position].scanedURL
            holder.message.text = cards[position].scanDate
        }

        override fun getItemCount(): Int {
            return cards.size
        }

        fun addCards(items: List<ScanedURL>) {
            cards.addAll(items)
        }
    }

    inner class TargetURLViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {
        override fun onClick(v: View?) {
            Log.e("A1tt", "clicked now")
        }

        var title: TextView = itemView.findViewById<View>(R.id.card_title) as TextView
        var message: TextView = itemView.findViewById<View>(R.id.card_message) as TextView

    }

}
