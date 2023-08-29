package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgs.manthara.databinding.FragmentJewellOfferBinding

class JewellOfferFragment : Fragment() {
    private lateinit var binding: FragmentJewellOfferBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJewellOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

}