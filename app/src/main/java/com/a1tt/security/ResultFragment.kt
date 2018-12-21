package com.a1tt.security

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList

class ResultFragment : Fragment() {

    private var cardsRecycler: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Analyses result"
        val view = inflater.inflate(R.layout.cards_fragment_layout, container, false)
        cardsRecycler = view.findViewById<View>(R.id.cards_recycler) as RecyclerView
        cardsRecycler!!.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val adapter = CardsAdapter()
        cardsRecycler!!.adapter = adapter
        adapter.addCards(createCards())
        adapter.notifyDataSetChanged()
        return view
    }

    private fun createCards(): List<Card> {
        val cards = ArrayList<Card>()
        for (i in 0..9) {
            val card = Card("Title $i", "Message $i")
            cards.add(card)
        }
        return cards
    }
}
