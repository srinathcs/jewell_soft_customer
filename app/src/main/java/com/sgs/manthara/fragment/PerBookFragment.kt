package com.sgs.manthara.fragment

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.manthara.R
import com.sgs.manthara.adapter.PerBookAdapter
import com.sgs.manthara.databinding.FragmentPerBookBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class PerBookFragment : Fragment() {
    private lateinit var binding: FragmentPerBookBinding
    private lateinit var mainPreference: MainPreference
    private lateinit var myadapter: PerBookAdapter
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private var productId = ""
    private var lt = ""
    private var ln = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerBookBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())
        productId = requireArguments().getString("productId")!!
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }
        perBook()
        wishToOrder()
        binding.btnSubmit.setOnClickListener {
            findNavController().navigate(R.id.newDesginReqFragment)
        }
        return binding.root
    }

    private fun wishToOrder() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.preBook(
                "24",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                productId
            )
        }
        wishToOrderResponse()
    }

    private fun wishToOrderResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.preBookFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "addPerBookError: ${it.message}")

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "addPerBook: ${it.data}")
                        if (it.data!!.error == false) {
                            Toast.makeText(
                                requireContext(),
                                "Add On Orders",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.tickFragment)
                        }
                    }
                }
            }
        }
    }
    private fun perBook() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.showPerBook(
                "25",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first()
            )
            Log.i("TAG", "perBook:${mainPreference.getUserId().first()} ")
        }
        perBookResponse()
    }

    private fun perBookResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.showPerBookFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "perBookResponseError: ${it.message}")

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "perBookResponse: ${it.data}")
                        myadapter = PerBookAdapter(requireContext())
                        binding.rvView.adapter = myadapter
                        binding.rvView.layoutManager = LinearLayoutManager(requireContext())
                        myadapter.differ.submitList(it.data)
                    }
                }
            }
        }
    }
}
