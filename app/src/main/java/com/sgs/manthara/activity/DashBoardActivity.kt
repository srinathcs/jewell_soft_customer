package com.sgs.manthara.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.sgs.manthara.R
import com.sgs.manthara.databinding.ActivityDashBoardBinding
import com.sgs.manthara.location.Constant
import com.sgs.manthara.location.FusedLocationService
import com.sgs.manthara.location.MyReceiver
import com.sgs.manthara.location.displayLocationSettingsRequest
import com.sgs.manthara.location.getLocationStatus

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var myReceiver: MyReceiver
    private var hasLocationPermission = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        setContentView(binding.root)

        myReceiver = MyReceiver()

        getLocation()

        val callback = this@DashBoardActivity.onBackPressedDispatcher.addCallback(this) {
            finish()
        }

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

    }

    private fun getLocation() {

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->

                hasLocationPermission =
                    permission[Manifest.permission.ACCESS_FINE_LOCATION] ?: hasLocationPermission

                if (!hasLocationPermission) {
                    finish()

                } else {
                    showLocationServicePermission()
                    startLocationService()
                }

            }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
            )
        )

    }

    private fun startLocationService() {

        Intent(this, FusedLocationService::class.java).also {
            it.action = Constant.ACTION_START_FUSED_SERVICE
            startService(it)
        }

    }

    private fun stopLocationServices() {

        Intent(this, FusedLocationService::class.java).also {
            it.action = Constant.ACTION_STOP_FUSED_SERVICE
            startService(it)
        }

    }

    private fun showLocationServicePermission() {
        if (!getLocationStatus(this)) {
            displayLocationSettingsRequest(this, this)
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationService()

    }


    override fun onPause() {
        super.onPause()
        stopLocationServices()
    }

    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(myReceiver, intentFilter)
        super.onStart()

    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        private const val FLEXIBLE_APP_UPDATE_REQ_CODE = 123
    }
}