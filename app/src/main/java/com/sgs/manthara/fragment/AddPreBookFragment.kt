package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.sgs.manthara.R
import com.sgs.manthara.adapter.ViewPagerAdapter
import com.sgs.manthara.databinding.FragmentAddPreBookBinding
import com.sgs.manthara.databinding.WishlistViewBinding

class AddPreBookFragment : Fragment() {
    private lateinit var binding: FragmentAddPreBookBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private val images = listOf(R.drawable.ic_one, R.drawable.ic_two, R.drawable.ic_three)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPreBookBinding.inflate(inflater, container, false)
        val adapter = ViewPagerAdapter(images)
        viewPager = binding.view
        dotsLayout = binding.dotsLayout
        viewPager.adapter = adapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })

        createDots()
        return binding.root
    }

    private fun updateDots(currentPosition: Int) {
        for (i in 0 until dotsLayout.childCount) {
            val dot = dotsLayout.getChildAt(i) as ImageView
            dot.setImageResource(
                if (i == currentPosition) R.drawable.ic_dot_filled else R.drawable.ic_viewpage_empty
            )
        }
    }

    private fun createDots() {
        for (i in images.indices) {
            val dot = ImageView(requireContext())
            dot.setImageResource(R.drawable.ic_viewpage_empty)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(6, 0, 6, 0)
            dotsLayout.addView(dot, params)
        }
        updateDots(0)
    }

}