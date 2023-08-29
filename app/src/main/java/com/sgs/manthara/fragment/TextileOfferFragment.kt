package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgs.manthara.databinding.FragmentTextileOfferBinding

class TextileOfferFragment : Fragment() {
    private lateinit var binding: FragmentTextileOfferBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentTextileOfferBinding.inflate(inflater, container, false)
        return binding.root

    }
}