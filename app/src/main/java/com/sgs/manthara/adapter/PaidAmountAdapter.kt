package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sgs.manthara.databinding.PaidAmountViewBinding
import com.sgs.manthara.jewelRetrofit.PaidAmount

class PaidAmountAdapter(val context: Context) :
    RecyclerView.Adapter<PaidAmountAdapter.PaidAmountViewHolder>() {

    var dashboardListener: ((locationModel: PaidAmount) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaidAmountAdapter.PaidAmountViewHolder {
        return PaidAmountViewHolder(
            PaidAmountViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PaidAmountAdapter.PaidAmountViewHolder, position: Int) {
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

    inner class PaidAmountViewHolder(private var binding: PaidAmountViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(view: PaidAmount) {
            binding.tvDate.text = view.due_date
            binding.tvSchemeIdValue.text = view.sch_type
            binding.tvSchemeValue.text = view.sch_name
            binding.tvPaidAmount.text = view.amount
            binding.tvDue.text = view.due_no
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

    private val callback = object : DiffUtil.ItemCallback<PaidAmount>() {
        override fun areItemsTheSame(oldItem: PaidAmount, newItem: PaidAmount): Boolean {
            return oldItem.chit_id == newItem.chit_id
        }

        override fun areContentsTheSame(oldItem: PaidAmount, newItem: PaidAmount): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)

}