package com.sgs.manthara.fragment

import android.content.Intent
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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sgs.manthara.R
import com.sgs.manthara.activity.DashBoardActivity
import com.sgs.manthara.adapter.NewArrivalsJewelAdapter
import com.sgs.manthara.databinding.FragmentNewArrivalsJewellBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class NewArrivalsJewellFragment : Fragment() {
    private lateinit var binding: FragmentNewArrivalsJewellBinding
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }

    private lateinit var mainPreference: MainPreference
    private lateinit var myadapter: NewArrivalsJewelAdapter
    private var lt = ""
    private var ln = ""
    private var id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewArrivalsJewellBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val int = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(int)
            requireActivity().finish()
        }

        binding.ivBack.setOnClickListener {
            val intent = Intent(requireActivity(), DashBoardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.tvTextiles.setOnClickListener {
            findNavController().navigate(R.id.textileFragment)
        }
        binding.tvJewell.setOnClickListener {
            findNavController().navigate(R.id.newArrivalsJewellFragment)

        }
        binding.vView.visibility = View.GONE
        binding.vView2.visibility = View.VISIBLE
        newArrivalsJewell()
        return binding.root
    }

    /*private fun textileArrivals() {
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
                        Log.i("TAG", "newArrivalTextileSuccess: ${it.message}")
                        myadapter = JewellOffersAdapter(requireContext())
                        binding.rvView.adapter = myadapter
                        binding.rvView.layoutManager = GridLayoutManager(requireContext(), 2)
                        myadapter.differ.submitList(it.data)
                    }
                }
            }
        }

    }*/

    private fun newArrivalsJewell() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.newJewellArrival(
                "21",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                "1"
            )
        }
        newArrival()
    }

    private fun newArrival() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.newJewellArrivalFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "newArrivalError: ${it.message}")

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "newArrivalSuccess: ${it.message}")
                        myadapter = NewArrivalsJewelAdapter(requireContext())
                        binding.rvView.adapter = myadapter
                        binding.rvView.layoutManager = GridLayoutManager(requireContext(), 2)
                        myadapter.differ.submitList(it.data)
                        wishList()
                        myadapter.dashboardListener = {
                            id = it.id
                            val bundle = Bundle()
                            bundle.putString("id", it.id)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.addPreBookFragment, bundle)
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
}