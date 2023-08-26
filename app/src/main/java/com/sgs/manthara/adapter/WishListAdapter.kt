package com.sgs.manthara.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgs.manthara.databinding.WishlistViewBinding

class WishListAdapter(val context: Context) :
    RecyclerView.Adapter<WishListAdapter.WishListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishListAdapter.WishListViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: WishListAdapter.WishListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class WishListViewHolder(private var binding: WishlistViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}