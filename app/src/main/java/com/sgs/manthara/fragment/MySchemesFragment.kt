package com.sgs.manthara.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.manthara.activity.DashBoardActivity
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

        mainPreference = MainPreference(requireContext())

        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }


        binding.btnJewlle.setOnClickListener {
            binding.clLayout.visibility = View.VISIBLE
            binding.llLayouts.visibility = View.GONE
            mySchemeType()
        }

        binding.btnTextile.setOnClickListener {
            binding.clLayout.visibility = View.VISIBLE
            binding.llLayouts.visibility = View.GONE
            myTextileType()
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
                mainPreference.getUserId().first(),
                "1"
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

    private fun myTextileType() {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.textilesType(
                "14",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                mainPreference.getUserId().first(),
                "2"
            )
        }
        myTextileSheme()
    }

    private fun myTextileSheme() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.textileTypeFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "mySheme121:${it.message} ")
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

                        if (it.data!!.isNotEmpty()) {
                            myadapter.dashboardListener?.invoke(it.data[0])
                            details(it.data[0].id)
                        }

                        myadapter.dashboardListener = { chitAccount ->
                            val id = chitAccount.id
                            textlieDetails(id)
                        }
                    }
                }
            }
        }
    }

    private fun textlieDetails(id: String) {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.textilesDetails(
                "15",
                mainPreference.getCid().first(),
                deviceId,
                ln,
                lt,
                id,
                mainPreference.getUserId().first()
            )
        }
        textlieDetaileResponse()
    }

    private fun textlieDetaileResponse() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.textileDetailsFlow.collect {
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
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
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
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}