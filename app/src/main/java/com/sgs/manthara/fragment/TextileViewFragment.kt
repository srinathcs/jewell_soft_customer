package com.sgs.manthara.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sgs.manthara.R
import com.sgs.manthara.adapter.ProductImageViewer
import com.sgs.manthara.databinding.FragmentTextileViewBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class TextileViewFragment : Fragment() {
    private lateinit var binding: FragmentTextileViewBinding
    private var idTextile = ""
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var mainPreference: MainPreference
    private lateinit var viewPager: ViewPager2
    private var lt = ""
    private var ln = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextileViewBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }
        idTextile = requireArguments().getString("idTextile")!!

        Log.i("TAG", "onCreateView:${idTextile} ")
        viewTextileItem()
        binding.btnAdd.setOnClickListener {
            addPerBook()
        }
        binding.ibView.setOnClickListener {
            findNavController().navigate(R.id.viewPage)
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
           findNavController().navigate(R.id.newArrivalsJewellFragment)
        }
        return binding.root
    }

    private fun addPerBook() {
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
                idTextile
            )
        }
        addPerBookResponse()
    }

    private fun addPerBookResponse() {
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

    @SuppressLint("HardwareIds")
    private fun viewTextileItem() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.selectedTextile(
                "22",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                idTextile,
            )
        }
        viewItemRespones()
    }

    private fun viewItemRespones() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.selectedTextileFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "viewItemError: ${it.message}")

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "viewItem: ${it.data}")

                        val adapter = ProductImageViewer(requireContext(), it.data!!.img)
                        viewPager = binding.view
                        viewPager.adapter = adapter

                        val dotsIndicator = binding.dotsIndicator
                        dotsIndicator.attachTo(viewPager)
                        binding.tvGrams.text = it.data.`0`.proquan
                        binding.tvNeck.text = it.data.`0`.proname
                        binding.tvPrice.text = it.data.`0`.proprice
                    }
                }
            }
        }
    }
}