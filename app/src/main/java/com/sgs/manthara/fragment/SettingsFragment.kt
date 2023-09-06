package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentSettingsBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mainPreference: MainPreference

    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private var lt = ""
    private var ln = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())

        binding.llNine.setOnClickListener {
            findNavController().navigate(R.id.logout)
        }
        binding.ibView.setOnClickListener {
            findNavController().navigate(R.id.viewPage)
        }
        binding.llFour.setOnClickListener {

        }

        return binding.root
    }



}