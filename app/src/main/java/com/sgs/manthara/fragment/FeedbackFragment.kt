package com.sgs.manthara.fragment

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
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentFeedbackBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class FeedbackFragment : Fragment() {
    private lateinit var binding: FragmentFeedbackBinding
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
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        mainPreference = MainPreference(requireContext())

        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }

        binding.btnSubmit.setOnClickListener {
            submit()
        }
        binding.ibView.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.settingsFragment)
        }

        return binding.root
    }

    private fun submit() {
        lifecycleScope.launchWhenStarted {
            val deviceId =
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )

            jewelSoftVM.feedback(
                "33",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                mainPreference.getLedger().first(),
                binding.etDescription.text.toString()

            )
            submitResponse()
        }
    }

    private fun submitResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.feedbackFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "submitResponse:${it.message} ")

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "submitResponse:${it.data}")
                        Toast.makeText(requireContext(), "Feedback Submitted", Toast.LENGTH_SHORT)
                            .show()
                        binding.etDescription.setText("")
                        findNavController().navigate(R.id.settingsFragment)
                    }
                }
            }
        }
    }

}