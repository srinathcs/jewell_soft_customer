package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.manthara.databinding.WishlistViewBinding
import com.sgs.manthara.jewelRetrofit.ShowWishList

class WishListAdapter(val context: Context) :
    RecyclerView.Adapter<WishListAdapter.WishListViewHolder>() {
    var dashboardListener: ((locationModel: ShowWishList) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishListAdapter.WishListViewHolder {
        return WishListViewHolder(
            WishlistViewBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: WishListAdapter.WishListViewHolder, position: Int) {
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

    inner class WishListViewHolder(private var binding: WishlistViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(view: ShowWishList) {
            val final = view.img1!!.replace("..", "")
            Glide.with(context).load(final).into(binding.ivImg)
            binding.tvModel.text = "Name : ${view.proname}"
            binding.tvPrice.text = "Price : ${view.proprice}"
            binding.tvMetrail.text = "Product : ${view.product_cat}"
        }

        init {

            binding.btnPreBook.setOnClickListener {
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

    private val callback = object : DiffUtil.ItemCallback<ShowWishList>() {
        override fun areItemsTheSame(
            oldItem: ShowWishList,
            newItem: ShowWishList
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShowWishList,
            newItem: ShowWishList
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, callback)
}