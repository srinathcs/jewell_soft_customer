package com.sgs.manthara.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sgs.manthara.R
import com.sgs.manthara.databinding.FragmentThemeBinding
import com.sgs.manthara.databinding.FragmentTickBinding

class ThemeFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentThemeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentThemeBinding.inflate(inflater, container, false)
        themes()
        return binding.root
    }

    private fun themes() {
        val layout = layoutInflater.inflate(R.layout.fragment_theme, null)
        binding = FragmentThemeBinding.bind(layout)
        /*alertDialog.setView(layout)
        alertDialog.show()*/

        binding.themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.lightThemeRadioButton -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )

                R.id.darkThemeRadioButton -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        binding.close.setOnClickListener {
            //alertDialog.dismiss()
            dismiss()
        }
    }
}