package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sgs.manthara.databinding.PayDueViewBinding
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.PendingDue
import kotlinx.coroutines.flow.first

class PayDueAdapter(val context: Context) :
    RecyclerView.Adapter<PayDueAdapter.PayDueViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PayDueAdapter.PayDueViewHolder {
        return PayDueViewHolder(
            PayDueViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PayDueAdapter.PayDueViewHolder, position: Int) {
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

    inner class PayDueViewHolder(private var binding: PayDueViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(view: PendingDue) {
            if (view.due_no == null) {
                // If dueno is null, show the message
                binding.tvDue.text = view.message
                binding.tvDueIn1.text = ""
                binding.tvChitName1.text = ""
                binding.tvChitId1.text = ""
                binding.tvDueno.text = ""
            } else {
                // If dueno is not null, show the dues information
                binding.tvDue1.text = view.amount
                binding.tvDueIn1.text = view.due_date
                binding.tvChitName1.text = view.sch_type
                binding.tvChitId1.text = view.sch_name
                binding.tvDueno.text = view.due_no
            }
        }

    }

    private val callback = object : DiffUtil.ItemCallback<PendingDue>() {
        override fun areItemsTheSame(oldItem: PendingDue, newItem: PendingDue): Boolean {
            return oldItem.sch_name == newItem.sch_name
        }

        override fun areContentsTheSame(oldItem: PendingDue, newItem: PendingDue): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)
}