package com.sgs.manthara.fragment

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.manthara.R
import com.sgs.manthara.adapter.WishListAdapter
import com.sgs.manthara.databinding.FragmentWishListBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class WishListFragment : Fragment() {
    private lateinit var binding: FragmentWishListBinding
    private lateinit var myAdapter: WishListAdapter
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var mainPreference: MainPreference
    private var lt = ""
    private var ln = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishListBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")
        }
        showWishList()
        return binding.root
    }

    private fun showWishList() {
        lifecycleScope.launchWhenStarted {
            val deviceId = Settings.Secure.getString(
                requireContext().contentResolver, Settings.Secure.ANDROID_ID
            )
            jewelSoftVM.showWishList(
                "29",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first()
            )
        }
        wishListRespones()
    }

    private fun wishListRespones() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.showWishListFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "wishListRespones:${it.message} ")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "wishListRespones:${it.data} ")
                        myAdapter = WishListAdapter(requireContext())
                        binding.rvView.adapter = myAdapter
                        binding.rvView.layoutManager = LinearLayoutManager(requireContext())
                        myAdapter.differ.submitList(it.data)

                        myAdapter.dashboardListener = {
                            val bundle = Bundle()
                            bundle.putString("productId", it.id)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.perBookFragment, bundle)
                        }
                    }
                }
            }
        }
    }
}