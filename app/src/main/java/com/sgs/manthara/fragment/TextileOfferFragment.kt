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
import com.sgs.manthara.adapter.TextileOfferAdapter
import com.sgs.manthara.databinding.FragmentTextileOfferBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class TextileOfferFragment : Fragment() {
    private lateinit var binding: FragmentTextileOfferBinding
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var mainPreference: MainPreference
    private lateinit var myadapter: TextileOfferAdapter
    private var lt = ""
    private var ln = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextileOfferBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }
        textileOffer()
        return binding.root

    }

    private fun textileOffer() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.offerTextile(
                "27",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                "4"
            )
        }
        textileResponse()
    }

    private fun textileResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.offerTextileFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "textileResponse:${it.message} ")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "textileResponse:${it.data} ")
                        myadapter = TextileOfferAdapter(requireContext())
                        binding.rvView.adapter = myadapter
                        binding.rvView.layoutManager = GridLayoutManager(requireContext(), 2)
                        myadapter.differ.submitList(it.data)
                    }
                }
            }
        }
    }
}