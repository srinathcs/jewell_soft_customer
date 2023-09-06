package com.sgs.manthara.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.sgs.manthara.databinding.FragmentAoubtUsBinding
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

@Suppress("DEPRECATION")
class AboutUsFragment : Fragment() {
    private lateinit var binding: FragmentAoubtUsBinding
    private lateinit var versionName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAoubtUsBinding.inflate(inflater, container, false)
        val tvDisplay: TextView = binding.osVersionNumber
        tvDisplay.text = getOsVersionDetail()
        getAppVersion()

        checkAppUpdate()

        return binding.root
    }

    private fun getOsVersionDetail(): String {
        return " ${Build.BRAND} " +
                " ${Build.VERSION.RELEASE} \n"

    }

    private fun checkAppUpdate() {
        binding.appUpdateLayout.setOnClickListener {
            val appUpdateManager = AppUpdateManagerFactory.create(requireContext())
            val appUpdateInfo = appUpdateManager.appUpdateInfo

            appUpdateInfo.addOnSuccessListener { updateInfo ->
                if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && updateInfo.isUpdateTypeAllowed(
                        AppUpdateType.FLEXIBLE
                    )
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        updateInfo,
                        AppUpdateType.FLEXIBLE,
                        requireActivity(),
                        1001
                    )
                } else {
                    Toast.makeText(requireContext(), "No Update Available", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            appUpdateInfo.addOnFailureListener {
                Log.i("TAG", "checkAppUpdate:${it.localizedMessage} ")
            }
        }

    }


    private fun getAppVersion() {
        try {

            val packageInfo =
                requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            versionName = packageInfo.versionName
            binding.appVersion.text = versionName

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}