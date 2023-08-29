package com.sgs.manthara.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgs.manthara.databinding.JewellOfferViewBinding

class JewellOffersAdapter(val context: Context) :
    RecyclerView.Adapter<JewellOffersAdapter.JewellOffersViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JewellOffersAdapter.JewellOffersViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: JewellOffersAdapter.JewellOffersViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class JewellOffersViewHolder(private var binding: JewellOfferViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}