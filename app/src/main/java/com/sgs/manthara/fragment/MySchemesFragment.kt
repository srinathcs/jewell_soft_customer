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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.manthara.databinding.FragmentMySchemesBinding
import com.sgs.manthara.adapter.MySchemeAdapter
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first
import java.lang.NullPointerException

class MySchemesFragment : Fragment() {
    private lateinit var binding: FragmentMySchemesBinding
    private lateinit var myadapter: MySchemeAdapter
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
        binding = FragmentMySchemesBinding.inflate(inflater, container, false)
        mySchemeType()
        mainPreference = MainPreference(requireContext())

        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }

        return binding.root
    }

    private fun mySchemeType() {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.schemeType(
                "14",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first()
            )
        }
        mySheme()
    }

    private fun mySheme() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.schemeTypeFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "mySheme11:${it.message} ")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "productShowsSuccess:${it.data}")
                        myadapter = MySchemeAdapter(requireContext())
                        binding.rvView.adapter = myadapter
                        binding.rvView.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )

                        myadapter.differ.submitList(it.data)
                        myadapter.notifyDataSetChanged()

                        if (it.data!!.isNotEmpty()) {
                            myadapter.dashboardListener?.invoke(it.data[0])
                            details(it.data[0].id)
                        }

                        myadapter.dashboardListener = { chitAccount ->
                            val id = chitAccount.id
                            details(id)
                        }
                    }
                }
            }
        }
    }

    private fun details(id: String) {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.schemeDetails(
                "15",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                id,
                mainPreference.getUserId().first()
            )
        }
        schemeDetails()
    }

    private fun schemeDetails() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.schemeDetFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "mySheme:${it.message} ")
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "schemeDetails:${it.data} ")
                        try {
                            val i = it.data!!
                            binding.tvName.text = i!!.name
                            binding.tvEmi.text = i.emi
                            binding.tvFreeEmi.text = i.free_emi
                            binding.tvPurity.text = i.purity
                            binding.tvType.text = i.scheme_type
                        }catch (e:NullPointerException){
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}