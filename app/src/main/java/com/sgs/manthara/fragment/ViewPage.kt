package com.sgs.manthara.fragment

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sgs.manthara.R
import com.sgs.manthara.adapter.ViewPagerAdapter
import com.sgs.manthara.databinding.FragmentViewPageBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import kotlinx.coroutines.flow.first

class ViewPage : Fragment(R.layout.fragment_view_page) {

    private var lt = ""
    private var ln = ""
    private lateinit var binding: FragmentViewPageBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var mainPreference: MainPreference
    private val images = listOf(R.drawable.natre, R.drawable.natural, R.drawable.images)
    private val jewelSoftVM: JewelVM by lazy {
        val repos = JewelRepo()
        val factory = JewelFactory(repos)
        ViewModelProvider(this, factory)[JewelVM::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPageBinding.inflate(inflater,container,false)

        viewPager = binding.view
        dotsLayout = binding.dotsLayout
        goldRate()
        silverRate()
        diamondRate()
        platinumRate()
        viewPager()
        mainPreference = MainPreference(requireContext())



        location()

        createDots()

        binding.imJoinNew.setOnClickListener {
            findNavController().navigate(R.id.joinNew)
        }

        binding.tvJoinNew.setOnClickListener {
            findNavController().navigate(R.id.joinNew)
        }

        binding.imMyScheme.setOnClickListener {
            findNavController().navigate(R.id.myScheme)
        }

        binding.imPayDue.setOnClickListener {
            findNavController().navigate(R.id.payDue)
        }

        binding.imPaidAmount.setOnClickListener {
            findNavController().navigate(R.id.paidAmountFragment)
        }

        binding.ivNewArr.setOnClickListener {
            findNavController().navigate(R.id.newArrivalsJewellFragment)
        }

        binding.ivSetting.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
        /*
                binding.ivMyWallet.setOnClickListener {
                    findNavController().navigate(R.id.myWallFragment)
                }*/
        binding.ivTotalWeight.setOnClickListener {
            findNavController().navigate(R.id.totalWeightFragment)
        }
        binding.ivOrder.setOnClickListener {
            findNavController().navigate(R.id.perBookFragment)
        }

        binding.ivJewellOffer.setOnClickListener {
            findNavController().navigate(R.id.jewellOfferFragment)
        }

        binding.ivWishList.setOnClickListener {
            findNavController().navigate(R.id.wishListFragment)
        }

        binding.ivTextileOffer.setOnClickListener {
            findNavController().navigate(R.id.textileOfferFragment)
        }
    return binding.root
    }

    private fun location(){
        FusedLocationService.latitudeFlow.observe(requireActivity()) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "viewPagerL:$lt")
            Log.i("TAG", "viewPagerLo:$ln")
        }
    }

    private fun createDots() {
        for (i in images.indices) {
            val dot = ImageView(requireContext())
            dot.setImageResource(R.drawable.ic_viewpage_empty)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(6, 0, 6, 0)
            dotsLayout.addView(dot, params)
        }
        updateDots(0)
    }

    private fun updateDots(currentPosition: Int) {
        for (i in 0 until dotsLayout.childCount) {
            val dot = dotsLayout.getChildAt(i) as ImageView
            dot.setImageResource(
                if (i == currentPosition) R.drawable.ic_dot_filled else R.drawable.ic_viewpage_empty
            )
        }
    }

    private fun goldRate() {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.rateGold(
                "9",
                "1",
                mainPreference.getCid().first(),
                deviceId,
                "555",
                "555",
                mainPreference.getUserId().first()
            )
        }
        goldToday()
    }

    private fun goldToday() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.todayRateGoldFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "mySheme11:${it.message} ")
                    }

                    is Resources.Success -> {
                        val i = it.data
                        binding.tvGold.text = i!!.rate
                        Log.i("TAG", "goldToday:${it.data} ")
                    }
                }
            }
        }
    }

    private fun silverRate() {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.rateSilver(
                "9",
                "2",
                mainPreference.getCid().first(),
                deviceId,
                "555",
                "555",
                mainPreference.getUserId().first()
            )
        }
        silverToday()
    }

    private fun silverToday() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.todayRateSliverFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "mySheme11:${it.message} ")
                    }

                    is Resources.Success -> {
                        val i = it.data
                        binding.tvSilver.text = i!!.rate
                        Log.i("TAG", "goldToday:${it.data} ")
                        //Log.i("TAG", "numberSuccess:${mainPreference.getLedger().first()} ")
                    }
                }
            }
        }
    }

    private fun diamondRate() {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.rateDiamond(
                "9",
                "3",
                mainPreference.getCid().first(),
                deviceId,
                "555",
                "555",
                mainPreference.getUserId().first()
            )
        }
        diamondToday()
    }

    private fun diamondToday() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.todayRateDiamondFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "mySheme11:${it.message} ")
                    }

                    is Resources.Success -> {
                        val i = it.data
                        binding.tvDiamond.text = i!!.rate
                        Log.i("TAG", "goldToday:${it.data} ")
                    }
                }
            }
        }
    }


    private fun platinumRate() {
        val deviceId =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.ratePlatinum(
                "9",
                "4",
                mainPreference.getCid().first(),
                deviceId,
                "555",
                "555",
                mainPreference.getUserId().first()
            )
        }
        platinumToday()
    }

    private fun platinumToday() {
        lifecycleScope.launchWhenStarted {
            jewelSoftVM.todayRatePlatinumFlow.collect {
                when (it) {
                    is Resources.Loading -> {

                    }

                    is Resources.Error -> {
                        Log.i("TAG", "mySheme11:${it.message} ")
                    }

                    is Resources.Success -> {
                        val i = it.data
                        binding.tvPlatinum.text = i!!.rate
                        Log.i("TAG", "goldToday:${it.data} ")
                    }
                }
            }
        }
    }

    private fun viewPager() {
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
                "5555",
                "5555",
                mainPreference.getUserId().first(),
                "1"
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
                        Log.i("TAG", "viewPagerResponse: ${it.data}")

                        val adapter = ViewPagerAdapter(requireContext(),it.data!!)
                        viewPager.adapter = adapter
                        mainPreference = MainPreference(requireContext())
                        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                                updateDots(position)
                            }
                        })
                    }
                }
            }
        }
    }
}