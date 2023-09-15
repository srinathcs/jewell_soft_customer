package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.manthara.databinding.WishlistViewBinding
import com.sgs.manthara.jewelRetrofit.ShowWishList

class WishListAdapter(val context: Context) :
    RecyclerView.Adapter<WishListAdapter.WishListViewHolder>() {
    var dashboardListener: ((locationModel: ShowWishList) -> Unit)? = null
    var closeListener: ((locationModel: ShowWishList) -> Unit)? = null
    private var dataList = arrayListOf<ShowWishList>()

    fun setList(dataList: List<ShowWishList>?) {
        dataList?.let {
            this.dataList = ArrayList(dataList)
            notifyItemRangeChanged(0, dataList.size)
        }
    }
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
            val statusModel = dataList[position]
            holder.setView(statusModel)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class WishListViewHolder(private var binding: WishlistViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setView(view: ShowWishList) {
            val final = view.img1.replace("..", "")
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
                        dashboardListener?.invoke(dataList[position])

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }

            binding.ivClose.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    try {
                        closeListener?.invoke(dataList[position])
                        dataList.removeAt(position)
                        notifyItemRemoved(position)

                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }

//            binding.ivClose.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    try {
//                        if (selectedPosition != position) {
////                            val previousSelectedPosition = selectedPosition
////                            selectedPosition = position
////                            notifyItemChanged(previousSelectedPosition)
////                            notifyItemChanged(selectedPosition)
////                            dashboardListener?.invoke(differ.currentList[selectedPosition])
////                            closeListener?.invoke(differ.currentList[position])
//                            closeListener?.invoke(differ.currentList[position])
//                        }
//                    }catch (e: NullPointerException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
        }
    }

    /*private val callback = object : DiffUtil.ItemCallback<ShowWishList>() {
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
    val differ = AsyncListDiffer(this, callback)*/
}