package com.sgs.manthara.adapter

import androidx.fragment.app.Fragment
import com.sgs.manthara.fragment.FirstFragment
import com.sgs.manthara.fragment.SecondFragment
import com.sgs.manthara.fragment.ThirdFragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class JewellSchemeAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstFragment()
            1 -> SecondFragment()
            else -> ThirdFragment()
        }
    }

}