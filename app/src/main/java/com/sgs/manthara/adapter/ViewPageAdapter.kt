package com.sgs.manthara.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgs.manthara.R
import com.sgs.manthara.jewelRetrofit.ViewPager

class ViewPagerAdapter(var context: Context, private val images: List<ViewPager>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageRes = images[position]
        holder.bind(imageRes.img!!)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(imageRes: String) {
            val final = imageRes.toString().replace("..", "")
            Glide.with(context).load(final).into(imageView)
        }
    }
}
