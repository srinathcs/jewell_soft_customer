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
import java.lang.Exception

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
    private var id = ""
    private var bookId = ""
    private var bookName = ""
    private var bookPrice = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerBookBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())

        try {
            productId = requireArguments().getString("productId")!!
            Log.i("TAG", "productId:${productId}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

        binding.ibView.setOnClickListener {
            findNavController().navigate(R.id.viewPage)
        }

        return binding.root
    }

    private fun wishToOrder() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
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
                            /*Toast.makeText(
                                requireContext(),
                                "Add On Orders",
                                Toast.LENGTH_SHORT
                            ).show()*/
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

                        myadapter.closeListener = {
                            id = it.id
                            closePerBookList()
                        }
                        myadapter.bookedListener = {
                            bookId = it.id
                            bookName = it.proname
                            bookPrice = it.proprice
                            booked()
                        }
                    }
                }
            }
        }
    }

    private fun closePerBookList() {
        lifecycleScope.launchWhenStarted {
            val deviceId = Settings.Secure.getString(
                requireContext().contentResolver, Settings.Secure.ANDROID_ID
            )
            jewelSoftVM.closeWishList(
                "31",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                id
            )
        }
        closeListRespones()
    }

    private fun closeListRespones() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.closeWishListFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "wishListRespones:${it.message} ")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "closeWishListRespones:${it.data} ")
                        Toast.makeText(requireContext(), "Delete Successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun booked() {
        lifecycleScope.launchWhenStarted {
            val deviceId = Settings.Secure.getString(
                requireContext().contentResolver, Settings.Secure.ANDROID_ID
            )
            jewelSoftVM.booked(
                "32",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                bookId,
                bookName,
                mainPreference.getLedger().first(),
                bookPrice
            )
        }
        bookedResponse()
    }

    private fun bookedResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.bookedFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "bookedErrorRespones:${it.message} ")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "bookedRespones:${it.data} ")
                        Toast.makeText(requireContext(), "Booked Successfully", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.tickFragment)
                    }
                }
            }
        }
    }
}