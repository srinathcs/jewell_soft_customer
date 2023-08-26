package com.sgs.manthara.activity

import android.content.Intent
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sgs.manthara.R
import com.sgs.manthara.databinding.ActivityOtpBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import com.sgs.manthara.new_otp.AppSignatureHashHelper
import kotlinx.coroutines.flow.first

class OtpActivity : AppCompatActivity(R.layout.activity_otp) {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var appSignatureHashHelper: AppSignatureHashHelper
    private lateinit var mainPreference: MainPreference
    private lateinit var otpViewModel: JewelVM
    private var resendTimer: CountDownTimer? = null
    private var timerRunning = false
    private val resendDelayMillis: Long = 30000
    private var lt = ""
    private var ln = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        validateField()
        startResendTimer()
        setContentView(binding.root)
        binding.btnSign.setOnClickListener {
            otp()
        }
        appSignatureHashHelper = AppSignatureHashHelper(this@OtpActivity)
        val repo = JewelRepo()
        val factory = JewelFactory(repo)
        otpViewModel = ViewModelProvider(this, factory)[JewelVM::class.java]
        mainPreference = MainPreference(this@OtpActivity)
        FusedLocationService.latitudeFlow.observe(this@OtpActivity) {
            lt = it.latitude.toString()
            ln = it.longitude.toString()
            Log.i("TAG", "onCreateL:$lt")
            Log.i("TAG", "onCreateLo:$ln")

        }
        binding.tvRecent.setOnClickListener {
            if (!timerRunning) {
                generateAndSendNewOTP()
                startResendTimer()
            }
        }

        binding.btnSign.paintFlags =
            binding.btnSign.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun validateField() {
        val fields = listOf(
            binding.et1,
            binding.et2,
            binding.et3,
            binding.et4,
            binding.et5,
            binding.et6
        )

        for (i in fields.indices) {
            val currentField = fields[i]
            val previousField = if (i > 0) fields[i - 1] else null
            val nextField = if (i < fields.size - 1) fields[i + 1] else null

            currentField.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (currentField.text.isEmpty() && previousField != null) {
                        currentField.clearFocus()
                        previousField.requestFocus()
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }

            currentField.doOnTextChanged { text, _, _, _ ->
                if (text?.length == 1 && nextField != null) {
                    nextField.requestFocus()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun otp() {
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val otp = binding.et1.text.toString().plus(binding.et2.text.toString())
            .plus(binding.et3.text.toString()).plus(binding.et4.text.toString())
            .plus(binding.et5.text.toString()).plus(binding.et6.text.toString())

        lifecycleScope.launchWhenStarted {
            otpViewModel.otp(
                "13",
                mainPreference.getCid().first(),
                deviceId,
                "55",
                "2",
                mainPreference.getUserId().first(),
                otp
            )
        }
        verify()
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun generateAndSendNewOTP() {
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        binding.tvRecent.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                otpViewModel.jewels(
                    "12",
                    mainPreference.getCid().first(),
                    deviceId,
                    ln,
                    lt,
                    mainPreference.getMobileNo().first(),
                    appSignatureHashHelper.getAppSignature()[0]
                )
            }
            numberSuccess()
        }
    }

    private fun numberSuccess() {
        lifecycleScope.launchWhenStarted {
            otpViewModel.testLoginFlow.collect {
                when (it) {
                    is Resources.Loading -> {
                        Log.i("TAG", "numberSuccessLoading:${it.message}")
                    }

                    is Resources.Error -> {
                        Log.i("TAG", "numberSuccessError:${it.message}")
                        Toast.makeText(
                            this@OtpActivity,
                            "Check the internet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resources.Success -> {
                        Log.i("TAG", "numberSuccess:${it.data}")

                    }
                }
            }
        }
    }

    private fun verify() {

        lifecycleScope.launchWhenStarted {
            otpViewModel.otpRecFlow.collect {
                when (it) {
                    is Resources.Loading -> {
                        Log.i("TAG", "verify1: ${it.data}")
                    }

                    is Resources.Success -> {
                        Log.e("TAG", "verifyOTP: ${it.data}")
                        if (it.data!!.error == false) {

                            mainPreference.saveLogin(true)
                            val role = "1"
                            mainPreference.saveRoleId(role)

                            intent = Intent(this@OtpActivity, DashBoardActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    is Resources.Error -> {

                        Log.i("TAG", "verify: ${it.message}")
                        Toast.makeText(this@OtpActivity, "Enter valid OTP", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun startResendTimer() {
        timerRunning = true
        resendTimer = object : CountDownTimer(resendDelayMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.tvRecent.text =
                    getString(R.string.resend_otp_countdown, secondsRemaining)
            }

            override fun onFinish() {
                timerRunning = false
                binding.tvRecent.setText(R.string.resend_otp)
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        resendTimer?.cancel()
    }
}