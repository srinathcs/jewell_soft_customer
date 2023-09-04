package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sgs.manthara.databinding.TotalWeightViewBinding
import com.sgs.manthara.jewelRetrofit.TotalWeight

class TotalWeightAdapter(val context: Context) :
    RecyclerView.Adapter<TotalWeightAdapter.TotalWeightViewHolder>() {
    var dashboardListener: ((locationModel: TotalWeight) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TotalWeightAdapter.TotalWeightViewHolder {
        return TotalWeightViewHolder(
            TotalWeightViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TotalWeightAdapter.TotalWeightViewHolder, position: Int) {
        try {
            val statusModel = differ.currentList[position]
            holder.setView(statusModel)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class TotalWeightViewHolder(private var binding: TotalWeightViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(view: TotalWeight) {
            binding.tvSchemeId.text = view.chit_id
            binding.tvSchemeName.text = view.sch_name
            binding.tvTotalWeight.text = view.total_weigth
            binding.tvPaidDues.text = view.paid_due
            binding.tvPaidAmount.text = view.paid
        }

        init {

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    try {
                        dashboardListener?.invoke(differ.currentList[position])

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }

        }

    }

    private val callback = object : DiffUtil.ItemCallback<TotalWeight>() {
        override fun areItemsTheSame(oldItem: TotalWeight, newItem: TotalWeight): Boolean {
            return oldItem.chit_id == newItem.chit_id
        }

        override fun areContentsTheSame(oldItem: TotalWeight, newItem: TotalWeight): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)
}