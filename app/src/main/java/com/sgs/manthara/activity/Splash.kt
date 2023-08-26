package com.sgs.manthara.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.sgs.manthara.R
import com.sgs.manthara.jewelRetrofit.MainPreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class Splash : AppCompatActivity() {

    private val SPLASH_DELAY: Long = 2000
    private lateinit var roleId: String
    private lateinit var roleType: String
    private lateinit var cid: String
    private var isLoggedIn = false
    private lateinit var mainPreference: MainPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mainPreference = MainPreference(this@Splash)

        logged()
    }

    private fun logged(){

        lifecycleScope.launchWhenStarted {
            roleId = mainPreference.getRoleId().first()
            roleType = mainPreference.getRoleType().first()
            cid = mainPreference.getCid().first()
            isLoggedIn = mainPreference.isLoggedIn().first()

            Log.i("TAG", "createCid: $cid")
            Log.i("TAG", "createRoleId: $roleId")
            Log.i("TAG", "createRoleType: $roleType")
            Log.i("TAG", "createIsLoggedIn: $isLoggedIn")

        }

        lifecycleScope.launchWhenStarted {
            delay(2000)

            when {
                isLoggedIn -> {

                    when(roleId){

                        "1" -> {
                            intent = Intent ( this@Splash, DashBoardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        "2"->{
                            intent = Intent ( this@Splash, DashBoardActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                    }

                }

                cid != "0" -> {
                    val intent = Intent(this@Splash, LoginWithOtpActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                else -> {
                    val intent = Intent(this@Splash,LoginWithOtpActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }

        }
    }
}