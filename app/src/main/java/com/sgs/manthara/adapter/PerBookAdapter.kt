package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.manthara.databinding.PerBookViewBinding
import com.sgs.manthara.jewelRetrofit.ShowPerBook
import com.sgs.manthara.jewelRetrofit.TotalWeight

class PerBookAdapter(val context: Context) :
    RecyclerView.Adapter<PerBookAdapter.PayBookViewHolder>() {
    var dashboardListener: ((locationModel: ShowPerBook) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PerBookAdapter.PayBookViewHolder {
        return PayBookViewHolder(
            PerBookViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PerBookAdapter.PayBookViewHolder, position: Int) {
        try {
            val statusModel = differ.currentList[position]
            holder.setView(statusModel)
            // Update tvItem with the position
            holder.binding.tvItem.text = "Item : ${(position + 1).toString()}"
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    inner class PayBookViewHolder(var binding: PerBookViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setView(view: ShowPerBook) {
            binding.tvModel.text = "Name : ${view.proname}"
            binding.tvPrice.text = "Price : ${view.proprice}"
            val final = view.img1!!.replace("..", "")
            Glide.with(context).load(final).into(binding.ivImg)

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

    private val callback = object : DiffUtil.ItemCallback<ShowPerBook>() {
        override fun areItemsTheSame(oldItem: ShowPerBook, newItem: ShowPerBook): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShowPerBook, newItem: ShowPerBook): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)
}