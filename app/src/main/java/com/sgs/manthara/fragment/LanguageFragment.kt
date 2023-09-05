package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentLanguageBinding
import com.sgs.manthara.utils.SettingPreference
import com.yariksoffice.lingver.Lingver

class LanguageFragment : Fragment() {
    private lateinit var binding: FragmentLanguageBinding
    private lateinit var settingPreference: SettingPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLanguageBinding.bind(view)
        settingPreference = SettingPreference(requireContext())

        // Call your language setup methods after Lingver initialization
        setAppLang()
        getAppLanguage()
    }

    private fun setAppLang() {
        binding.tamil.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                settingPreference.setAppLang("ta")
            }
            Lingver.getInstance().setLocale(requireActivity(), "ta")
            requireActivity().recreate()
        }

        binding.english.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                settingPreference.setAppLang("en")
            }
            Lingver.getInstance().setLocale(requireActivity(), "en")
            requireActivity().recreate()
        }
    }

    private fun getAppLanguage() {
        lifecycleScope.launchWhenStarted {
            settingPreference.getAppLang().collect {
                binding.tamil.isChecked = it == "ta"
                binding.english.isChecked = it == "en"
            }
        }
    }
}