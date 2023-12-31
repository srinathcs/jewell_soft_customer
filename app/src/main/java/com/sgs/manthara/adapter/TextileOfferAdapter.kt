package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.manthara.databinding.TexeileOfferViewBinding
import com.sgs.manthara.jewelRetrofit.OfferJewell

class TextileOfferAdapter(val context: Context) :
    RecyclerView.Adapter<TextileOfferAdapter.TextileOffersViewHolder>() {

    var dashboardListener: ((locationModel: OfferJewell) -> Unit)? = null
    var checkListener: ((locationModel: OfferJewell) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TextileOfferAdapter.TextileOffersViewHolder {
        return TextileOffersViewHolder(
            TexeileOfferViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: TextileOfferAdapter.TextileOffersViewHolder,
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

    inner class TextileOffersViewHolder(private var binding: TexeileOfferViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(view: OfferJewell) {
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

    private val callback = object : DiffUtil.ItemCallback<OfferJewell>() {
        override fun areItemsTheSame(
            oldItem: OfferJewell,
            newItem: OfferJewell
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: OfferJewell,
            newItem: OfferJewell
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)
}