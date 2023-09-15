package com.sgs.manthara.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.manthara.activity.DashBoardActivity
import com.sgs.manthara.adapter.PaidAmountAdapter
import com.sgs.manthara.databinding.FragmentPaidAmountBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class PaidAmountFragment : Fragment() {
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }
    private lateinit var binding: FragmentPaidAmountBinding
    private lateinit var mainPreference: MainPreference
    private lateinit var myadapter: PaidAmountAdapter
    private var lt = ""
    private var ln = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaidAmountBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }

        binding.ibView.setOnClickListener {
            val intent = Intent(requireActivity(), DashBoardActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val int = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(int)
            requireActivity().finish()
        }

        viewPaidAmount()
        return binding.root
    }

    private fun viewPaidAmount() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.paidAmount(
                "20",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first()
            )
        }
        paidAmountResponse()
    }

    private fun paidAmountResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.paidAmountFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "paidAmountResponseError: ${it.message}")

                    }

                    is Resources.Success -> {

                        if (it.data.isNullOrEmpty()) {
                            binding.noData.visibility = View.VISIBLE
                            binding.rvView.visibility = View.GONE
                        } else {
                            Log.i("TAG", "paidAmountResponse: ${it.data}")
                            myadapter = PaidAmountAdapter(requireContext())
                            binding.rvView.adapter = myadapter
                            binding.rvView.layoutManager = LinearLayoutManager(requireContext())
                            myadapter.differ.submitList(it.data)
                        }
                    }
                }
            }
        }
    }
}