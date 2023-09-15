package com.sgs.manthara.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sgs.manthara.databinding.ActivityMobileBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import com.sgs.manthara.new_otp.AppSignatureHashHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class LoginWithOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMobileBinding
    private lateinit var jewelVM: JewelVM
    private lateinit var intentSender: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var mainPreference: MainPreference
    private lateinit var appSignatureHashHelper: AppSignatureHashHelper
    private var token: String = "0"
    private var lt = ""
    private var ln = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainPreference = MainPreference(this)
        appSignatureHashHelper = AppSignatureHashHelper(this)
        val newsRepository = JewelRepo()
        val mainViewModelProviderFactory = JewelFactory(newsRepository)
        jewelVM = ViewModelProvider(this, mainViewModelProviderFactory)[JewelVM::class.java]
        FusedLocationService.latitudeFlow.observe(this@LoginWithOtpActivity) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }
        binding.btnSubmit.setOnClickListener {
            numberSend()
        }
        val callback = this@LoginWithOtpActivity.onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun numberSend() {
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        if (binding.etNum.text.toString().length != 10) {
            Toast.makeText(this, "Enter the correct number", Toast.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launchWhenStarted {
                jewelVM.jewels(
                    "12",
                    "21472147",
                    deviceId,
                    "5555",
                    "555",
                    binding.etNum.text.toString(),
                    appSignatureHashHelper.getAppSignature()[0]
                )
                mainPreference.saveMobileNo(binding.etNum.text.toString())
            }
            numberSuccess()
        }
    }

    private fun numberSuccess() {
        lifecycleScope.launchWhenStarted {
            jewelVM.testLoginFlow.collect {
                when (it) {
                    is Resources.Loading -> {
                        Log.i("TAG", "numberSuccessLoading:${it.message}")
                        binding.progress.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.GONE
                    }

                    is Resources.Error -> {
                        Log.i("TAG", "numberSuccessError:${it.message}")
                        Toast.makeText(
                            this@LoginWithOtpActivity,
                            "Check the internet",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.progress.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.GONE
                        delay(1000)
                        binding.progress.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE

                    }

                    is Resources.Success -> {
                        Log.i("TAG", "numberSuccess:${it.data}")
                        binding.progress.visibility = View.GONE
                        binding.btnSubmit.visibility = View.VISIBLE
                        if (it.data!!.error == false) {
                            if (it.data!!.error_msg == "Allow to next page") {
                                mainPreference.saveCId(it.data.cid)
                                mainPreference.saveUserId(it.data.cus_id)
                                mainPreference.saveLedgerId(it.data.led_id)
                                intent =
                                    Intent(this@LoginWithOtpActivity, OtpActivity::class.java)
                                startActivity(intent)

                                Log.i("TAG", "numberSuccess:${mainPreference.getCid().first()} ")
                            }
                        } else {
                            Toast.makeText(
                                this@LoginWithOtpActivity, "Your login is wrong", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}