package com.sgs.manthara.location

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.sgs.manthara.location.Constant.ACTION_START_FUSED_SERVICE
import com.sgs.manthara.location.Constant.ACTION_STOP_FUSED_SERVICE

class FusedLocationService : LifecycleService() {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    companion object {
        val latitudeFlow = MutableLiveData<Location>()
    }


    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()

        fusedLocationProviderClient= FusedLocationProviderClient(applicationContext)

        locationRequest = LocationRequest.create().apply {
            interval = 5000L
            fastestInterval = 2000L
            priority = PRIORITY_HIGH_ACCURACY
        }


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                latitudeFlow.value = locationResult.lastLocation

            }
        }


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                ACTION_START_FUSED_SERVICE -> {

                    requestLastLocation()
                }
                ACTION_STOP_FUSED_SERVICE -> {
                    stopRequestLocation()
                }
                else -> {
                    /*NO_OP*/
                }
            }
        }


        return super.onStartCommand(intent, flags, startId)
    }

    private fun requestLastLocation() {

        if (PermissionUtil.checkPermission(applicationContext)) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    latitudeFlow.value=it

                    Log.i("TAG", "requestLastLocation:${it.latitude} ")
                }
                requestCurrentLocation()
            }

        }

    }

    private fun stopRequestLocation() {
        stopSelf()
    }

    private fun requestCurrentLocation() {
        if (PermissionUtil.checkPermission(applicationContext)) {
            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(applicationContext)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}