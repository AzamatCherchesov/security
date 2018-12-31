package com.a1tt.security.AnalysResults

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import com.a1tt.security.R
import java.util.ArrayList

class ResultFragment : Fragment() {

    private var cardsRecycler: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Analyses result"
        (activity as AppCompatActivity).findViewById<View>(R.id.search)?.visibility = GONE

        val view = inflater.inflate(R.layout.cards_fragment_layout, container, false)
        cardsRecycler = view.findViewById<View>(R.id.cards_recycler) as RecyclerView
        cardsRecycler!!.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = CardsAdapter()
        cardsRecycler!!.adapter = adapter
        adapter.addCards(cards)
        adapter.notifyDataSetChanged()
        return view
    }

    companion object {
        val cards = ArrayList<Card>()
    }
}
