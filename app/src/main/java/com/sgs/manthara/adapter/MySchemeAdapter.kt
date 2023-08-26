package com.sgs.manthara.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sgs.manthara.R
import com.sgs.manthara.databinding.SchemeTypeViewBinding
import com.sgs.manthara.jewelRetrofit.SchemeDetails

class MySchemeAdapter(val context: Context) : RecyclerView.Adapter<MySchemeAdapter.HomeViewHolder>() {

    var dashboardListener: ((locationModel: SchemeDetails) -> Unit)? = null

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            SchemeTypeViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MySchemeAdapter.HomeViewHolder, position: Int) {
        val statusModel = differ.currentList[position]
        holder.setView(statusModel, position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class HomeViewHolder(private var binding: SchemeTypeViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val text: TextView = itemView.findViewById(R.id.tvChit1)

        fun setView(sample: SchemeDetails, position: Int) {
            binding.tvChit1.text = sample.name

            if (position == selectedPosition) {
                text.setBackgroundResource(R.drawable.under_line)
            } else {
                text.setBackgroundResource(R.drawable.ic_trans_underline)
            }

            binding.tvChit1.setOnClickListener { view ->
                val clickedPosition = adapterPosition
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    if (selectedPosition != clickedPosition) {
                        val previousSelectedPosition = selectedPosition
                        selectedPosition = clickedPosition
                        notifyItemChanged(previousSelectedPosition)
                        notifyItemChanged(selectedPosition)
                        dashboardListener?.invoke(differ.currentList[selectedPosition])
                        Log.i("TAG", "myClick:${sample.name}")
                    }
                }
            }
        }
    }

    private val callback = object : DiffUtil.ItemCallback<SchemeDetails>() {
        override fun areItemsTheSame(oldItem: SchemeDetails, newItem: SchemeDetails): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: SchemeDetails, newItem: SchemeDetails): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)
}