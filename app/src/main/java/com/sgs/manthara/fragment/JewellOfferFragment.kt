package com.sgs.manthara.fragment

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.sgs.manthara.adapter.JewellOffersAdapter
import com.sgs.manthara.databinding.FragmentJewellOfferBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class JewellOfferFragment : Fragment() {
    private lateinit var binding: FragmentJewellOfferBinding
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var mainPreference: MainPreference
    private lateinit var myadapter: JewellOffersAdapter
    private var lt = ""
    private var ln = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJewellOfferBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }
        jewelOffer()
        return binding.root
    }

    private fun jewelOffer() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.offerJewell(
                "27",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                "1"
            )
        }
        jewelRespones()
    }

    private fun jewelRespones() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.offerJewellFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "jewelRespones:${it.message}")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "jewelRespones:${it.data}")
                        myadapter = JewellOffersAdapter(requireContext())
                        binding.rvView.adapter = myadapter
                        binding.rvView.layoutManager = GridLayoutManager(requireContext(), 2)
                        myadapter.differ.submitList(it.data)
                    }
                }
            }
        }
    }
}