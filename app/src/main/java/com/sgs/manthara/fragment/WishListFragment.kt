package com.sgs.manthara.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sgs.manthara.databinding.FragmentWishListBinding


class WishListFragment : Fragment() {
    private lateinit var binding: FragmentWishListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentWishListBinding.inflate(inflater, container, false)
        
        return binding.root
    }

}