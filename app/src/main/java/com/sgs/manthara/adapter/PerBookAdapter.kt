package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgs.manthara.databinding.PerBookViewBinding

class PerBookAdapter(val context: Context) :
    RecyclerView.Adapter<PerBookAdapter.PayBookViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PerBookAdapter.PayBookViewHolder {
        return PayBookViewHolder(
            PerBookViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PerBookAdapter.PayBookViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class PayBookViewHolder(private var binding: PerBookViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}