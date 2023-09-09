package com.sgs.manthara.activity

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.sgs.manthara.R
import com.sgs.manthara.databinding.ActivityOtpBinding
import com.sgs.manthara.jewelRetrofit.JewelFactory
import com.sgs.manthara.jewelRetrofit.JewelRepo
import com.sgs.manthara.jewelRetrofit.JewelVM
import com.sgs.manthara.jewelRetrofit.MainPreference
import com.sgs.manthara.jewelRetrofit.Resources
import com.sgs.manthara.location.FusedLocationService
import com.sgs.manthara.new_otp.AppSignatureHashHelper
import com.sgs.manthara.new_otp.CheckJava
import com.sgs.manthara.new_otp.NewReceiver
import com.sgs.manthara.new_otp.SmsBroadcastReceiver
import kotlinx.coroutines.delay
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
    private val REQUEST_USER_CONSENT = 200
    private lateinit var newReceiver: NewReceiver
    private var smsBroadcastReceiverr: SmsBroadcastReceiver? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        validateField()
        startResendTimer()
        setContentView(binding.root)
        val callback = this@OtpActivity.onBackPressedDispatcher.addCallback(this) {
            val int = Intent(this@OtpActivity, LoginWithOtpActivity::class.java)
            startActivity(int)
            finish()
        }
        binding.btnSign.setOnClickListener {
            otp()
        }

        appSignatureHashHelper = AppSignatureHashHelper(this@OtpActivity)
        val repo = JewelRepo()
        val factory = JewelFactory(repo)
        newReceiver = NewReceiver()
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
        val otpFields = listOf(
            binding.et1, binding.et2, binding.et3,
            binding.et4, binding.et5, binding.et6
        )

        otpFields.forEachIndexed { index, editText ->
            editText.doOnTextChanged { text, _, _, _ ->
                if (text?.length == 1) {
                    if (index == otpFields.size - 1 && allOtpFieldsFilled()) {
                        binding.btnSign.setBackgroundResource(R.drawable.otp)
                        binding.btnSign.isEnabled = true
                    }
                } else {
                    binding.btnSign.setBackgroundResource(R.drawable.ic_background)
                    binding.btnSign.isEnabled = false
                }
            }
        }
        startSmartUserConsent()
        CheckJava.StartSmsListners(this)
        val l1 = object : NewReceiver.OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                Log.e("otptag", otp!!)
            }

            override fun onOTPTimeOut() {

            }

        }
        newReceiver.InjectListner(l1)
        this.registerReceiver(newReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))

        binding.btnSign.paintFlags =
            binding.btnSign.paintFlags or Paint.UNDERLINE_TEXT_FLAG

    }



    private fun allOtpFieldsFilled(): Boolean {
        val otpFields = listOf(
            binding.et1, binding.et2, binding.et3,
            binding.et4, binding.et5, binding.et6
        )

        return otpFields.all { it.text.isNotEmpty() }
    }

    private fun startSmartUserConsent() {

        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)
        client.startSmsUserConsent(null)
    }

    private fun registerBroadcastReceiver() {

        smsBroadcastReceiverr = SmsBroadcastReceiver()
        smsBroadcastReceiverr!!.smsBroadcastReceiverListener =
            object : SmsBroadcastReceiver.SMSBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {

                    if (intent != null) {
                        startActivityForResult(intent, REQUEST_USER_CONSENT)
                    }
                }

                override fun onFailure() {

                }

            }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        this.registerReceiver(smsBroadcastReceiverr, intentFilter)

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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getOtpMessage(message: String?) {
//        val otpPattern = Pattern.compile("(|^)\\d{6}")
//        val matcher = otpPattern.matcher(message.toString())
//        if (matcher.find()) {
//            binding.etOTP1.setText(matcher.group(0))
//            binding.etOTP2.setText(matcher.group(1))
//            binding.etOTP3.setText(matcher.group(2))
//            binding.etOTP4.setText(matcher.group(3))
//            binding.etOTP5.setText(matcher.group(4))
//            binding.etOTP6.setText(matcher.group(5))
//        }

        val otpRegex = Regex("\\b(\\d{6})\\b") // Matches a sequence of 6 digits
        val matchResult = message?.let { otpRegex.find(it) }
        val extractedOtp = matchResult?.groupValues?.get(1) ?: ""
        val extractedOtp1 = matchResult?.groupValues?.get(2) ?: ""
        val extractedOtp2 = matchResult?.groupValues?.get(3) ?: ""
        val extractedOtp3 = matchResult?.groupValues?.get(4) ?: ""
        val extractedOtp4 = matchResult?.groupValues?.get(5) ?: ""
        val extractedOtp5 = matchResult?.groupValues?.get(6) ?: ""
        binding.et1.setText(extractedOtp)
        binding.et2.setText(extractedOtp1)
        binding.et3.setText(extractedOtp2)
        binding.et4.setText(extractedOtp3)
        binding.et5.setText(extractedOtp4)
        binding.et6.setText(extractedOtp5)

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
                        binding.progress.visibility = View.VISIBLE
                        binding.btnSign.visibility = View.GONE
                    }

                    is Resources.Success -> {
                        Log.e("TAG", "verifyOTP: ${it.data}")
                        binding.progress.visibility = View.GONE
                        binding.btnSign.visibility = View.VISIBLE
                        if (it.data!!.error == false) {

                            mainPreference.saveLogin(true)
                            val role = "1"
                            mainPreference.saveRoleId(role)

                            intent = Intent(this@OtpActivity, DashBoardActivity::class.java)
                            startActivity(intent)
                        }
                    }

                    is Resources.Error -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.btnSign.visibility = View.GONE
                        delay(1000)
                        binding.progress.visibility = View.GONE
                        binding.btnSign.visibility = View.VISIBLE
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

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        this.unregisterReceiver(smsBroadcastReceiverr)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_USER_CONSENT) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                // Process the message to extract OTP and fill the OTP fields.
                fillOtpFieldsFromMessage(message)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fillOtpFieldsFromMessage(message: String?) {
        val otpRegex = Regex("\\b(\\d{6})\\b") // Matches a sequence of 6 digits
        val matchResult = message?.let { otpRegex.find(it) }
        val extractedOtp = matchResult?.groupValues?.get(1) ?: ""

        // Now fill the OTP fields with the extracted OTP digits
        if (extractedOtp.length >= 6) {
            binding.et1.setText(extractedOtp[0].toString())
            binding.et2.setText(extractedOtp[1].toString())
            binding.et3.setText(extractedOtp[2].toString())
            binding.et4.setText(extractedOtp[3].toString())
            binding.et5.setText(extractedOtp[4].toString())
            binding.et6.setText(extractedOtp[5].toString())
        }
    }

}