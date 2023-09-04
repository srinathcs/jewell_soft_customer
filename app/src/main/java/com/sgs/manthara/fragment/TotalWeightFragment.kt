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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.manthara.adapter.PaidAmountAdapter
import com.sgs.manthara.adapter.TotalWeightAdapter
import com.sgs.manthara.databinding.FragmentTotalWeightBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class TotalWeightFragment : Fragment() {
    private lateinit var binding: FragmentTotalWeightBinding
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var myadapter: TotalWeightAdapter
    private lateinit var mainPreference: MainPreference
    private var lt = ""
    private var ln = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTotalWeightBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }
        totalWeight()
        return binding.root
    }

    private fun totalWeight() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.totalWeight(
                "23",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                "91"
            )
        }
        totalWeightResponse()
    }

    private fun totalWeightResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.totalWeightFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "totalWeightError: ${it.message}")

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "totalWeight: ${it.data}")
                        myadapter = TotalWeightAdapter(requireContext())
                        binding.rvView.adapter = myadapter
                        binding.rvView.layoutManager = LinearLayoutManager(requireContext())
                        myadapter.differ.submitList(it.data)
                    }
                }
            }
        }
    }
}