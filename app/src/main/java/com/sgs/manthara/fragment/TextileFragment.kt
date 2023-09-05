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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sgs.manthara.R
import com.sgs.manthara.adapter.TextileAdapter
import com.sgs.manthara.databinding.FragmentTextileBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class TextileFragment : Fragment() {
    private lateinit var binding: FragmentTextileBinding
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var mainPreference: MainPreference
    private lateinit var myadapter: TextileAdapter
    private var lt = ""
    private var ln = ""
    private var id = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextileBinding.inflate(inflater, container, false)

        mainPreference = MainPreference(requireContext())
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }

        binding.tvJewell.setOnClickListener {
            findNavController().navigate(R.id.newArrivalsJewellFragment)
        }
        binding.vView2.visibility = View.GONE
        binding.vView.visibility = View.VISIBLE
        textileArrivals()
        return binding.root
    }

    private fun textileArrivals() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.newTextilesArrival(
                "21",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                "4"
            )
        }
        newJewellArrival()
    }

    private fun newJewellArrival() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.newTextilesArrivalFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "newArrivalTextileError: ${it.message}")

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "newArrivalTextileSuccess: ${it.data}")
                        myadapter = TextileAdapter(requireContext())
                        binding.rvView.adapter = myadapter
                        binding.rvView.layoutManager = GridLayoutManager(requireContext(), 2)
                        myadapter.differ.submitList(it.data)

                        myadapter.dashboardListener = {
                            val bundle = Bundle()
                            bundle.putString("idTextile", it.id)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.textileViewFragment, bundle)
                            Log.i("TAG", "newJewellArrival:${it.id} ")
                        }
                        myadapter.wishListener = {
                            id = it.id
                            wishList()
                            Toast.makeText(requireContext(), "Add to wishlist", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun wishList() {
        val deviceId =
            Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.wishList(
                "28",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                id
            )
        }
        wishListRespones()
    }

    private fun wishListRespones() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.wishListFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "wishListRespones:${it.message} ")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "wishListRespones:${it.data}")

                    }
                }
            }
        }
    }
}