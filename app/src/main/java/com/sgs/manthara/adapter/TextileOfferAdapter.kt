package com.sgs.manthara.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgs.manthara.databinding.TexeileOfferViewBinding

class TextileOfferAdapter (val context: Context) :
    RecyclerView.Adapter<TextileOfferAdapter.TextileOffersViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TextileOfferAdapter.TextileOffersViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: TextileOfferAdapter.TextileOffersViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class TextileOffersViewHolder(private var binding: TexeileOfferViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}