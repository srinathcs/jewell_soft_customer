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
import com.sgs.manthara.adapter.PayDueAdapter
import com.sgs.manthara.databinding.FragmentPayDueBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class PayDueFragment : Fragment() {
    private lateinit var binding: FragmentPayDueBinding
    private lateinit var myadapter: PayDueAdapter
    private lateinit var mainPreference: MainPreference
    private var lt = ""
    private var ln = ""
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainPreference = MainPreference(requireContext())
        binding = FragmentPayDueBinding.inflate(inflater, container, false)
        pendingDue()
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

        return binding.root
    }

    private fun pendingDue() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            jewelSoftVM.pendingDue(
                "17",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first()
            )
        }
        pendingResponse()
    }

    private fun pendingResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.pendingDueFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "pendingResponseError: ${it.message}")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "pendingResponse:${it.data} ")

                        var message = ""

                        for (i in it.data!!) {
                            message = i.message
                        }

                        if (message == "Current month no due is pending") {
                            binding.noDuesTextView.visibility = View.VISIBLE
                            binding.rvView.visibility = View.GONE
                        } else {
                            binding.noDuesTextView.visibility = View.GONE
                            binding.rvView.visibility = View.VISIBLE

                            myadapter = PayDueAdapter(requireContext())
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