package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.manthara.databinding.JewellOfferViewBinding
import com.sgs.manthara.jewelRetrofit.NewJewellArrival

class NewArrivalsJewelAdapter(val context: Context) :
    RecyclerView.Adapter<NewArrivalsJewelAdapter.JewellOffersViewHolder>() {
    var dashboardListener: ((locationModel: NewJewellArrival) -> Unit)? = null
    var checkListener: ((locationModel: NewJewellArrival) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewArrivalsJewelAdapter.JewellOffersViewHolder {
        return JewellOffersViewHolder(
            JewellOfferViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: NewArrivalsJewelAdapter.JewellOffersViewHolder,
        position: Int
    ) {
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

    inner class JewellOffersViewHolder(private var binding: JewellOfferViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(view: NewJewellArrival) {
            val final = view.img1!!.replace("..", "")
            Glide.with(context).load(final).into(binding.ivIcon)
            binding.tvName.text = view.proname
            binding.tvPrice.text = view.proprice

            binding.ivWish.isChecked = view.wishlist_status == "1"

        }

        init {

            binding.ivNext.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    try {
                        dashboardListener?.invoke(differ.currentList[position])

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }

            binding.ivWish.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    try {
                        checkListener?.invoke(differ.currentList[position])

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }

    private val callback = object : DiffUtil.ItemCallback<NewJewellArrival>() {
        override fun areItemsTheSame(
            oldItem: NewJewellArrival,
            newItem: NewJewellArrival
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NewJewellArrival,
            newItem: NewJewellArrival
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)
}