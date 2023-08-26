package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentNewArrivalsJewellBinding

class NewArrivalsJewellFragment : Fragment() {
    private lateinit var binding: FragmentNewArrivalsJewellBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewArrivalsJewellBinding.inflate(inflater, container, false)
        binding.tvTextiles.setOnClickListener {
            findNavController().navigate(R.id.textileFragment)
        }
        binding.tvJewell.setOnClickListener {
            findNavController().navigate(R.id.newArrivalsJewellFragment)
        }
        return binding.root
    }


}