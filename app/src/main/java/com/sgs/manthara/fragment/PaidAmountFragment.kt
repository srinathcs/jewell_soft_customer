package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentPaidAmountBinding

class PaidAmountFragment : Fragment() {
    private lateinit var binding: FragmentPaidAmountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaidAmountBinding.inflate(inflater, container, false)
        return binding.root
    }

}