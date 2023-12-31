package com.sgs.manthara.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.sgs.manthara.R
import com.sgs.manthara.activity.DashBoardActivity
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
    private var id = ""


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
        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), DashBoardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val int = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(int)
            requireActivity().finish()
        }

        img()
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
                "2"
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
                        wishList()
                        myadapter.dashboardListener = {
                            id = it.id
                            val bundle = Bundle()
                            bundle.putString("id", it.id)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.offerPerBookFragment, bundle)
                        }

                        myadapter.checkListener = {
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

    private fun img() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.viewPagers(
                "34",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                "2"
            )
        }
        viewPagerResponse()
    }

    private fun viewPagerResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.viewPagerFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "viewPagerResponseError: ${it.message}")

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "viewPagerResponse:${it.data} ")

                        for ( i in it.data!!){
                            val final = i.img!!.replace("..", "")
                            Glide.with(requireContext()).load(final).into(binding.ivOffer)
                        }


                    }
                }
            }
        }
    }
}