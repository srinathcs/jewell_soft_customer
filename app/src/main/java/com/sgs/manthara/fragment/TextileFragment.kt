package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentTextileBinding

class TextileFragment : Fragment() {
    private lateinit var binding: FragmentTextileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTextileBinding.inflate(inflater, container, false)

        binding.tvJewell.setOnClickListener {
            findNavController().navigate(R.id.newArrivalsJewellFragment)
        }
        binding.vView2.visibility = View.GONE
        binding.vView.visibility = View.VISIBLE

        return binding.root
    }
}