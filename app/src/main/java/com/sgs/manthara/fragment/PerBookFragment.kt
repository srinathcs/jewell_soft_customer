package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentPerBookBinding

class PerBookFragment : Fragment() {
private lateinit var binding: FragmentPerBookBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentPerBookBinding.inflate(inflater,container,false)
        binding.btnSubmit.setOnClickListener {
            findNavController().navigate(R.id.newDesginReqFragment)
        }
        return binding.root
    }
}