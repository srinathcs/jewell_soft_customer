package com.sgs.manthara.activity

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sgs.manthara.databinding.ActivitySigninBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvText3.paintFlags = binding.tvText3.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }
}