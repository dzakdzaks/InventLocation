package com.dzakdzaks.invent_location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import java.io.IOException
import java.util.*


/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 17 August 2020 at 2:17 PM.
 * Project Name => Bunda Mart Customer
 * Package Name => com.invent.bmcustomer.utils.helper
 * ==================================//==================================
 * ==================================//==================================
 */

class InventLocation(
    private val activity: Activity,
    private val inventLocationListener: InventLocationListener?
) {

    val TAG = "InventLocation"
    private var context: Context? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    companion object {
        const val REQUEST_ENABLE_GPS = 100

        fun isLocationServiceEnabled(context: Context): Boolean {
            val locatioManager: LocationManager? =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return if (locatioManager != null)
                LocationManagerCompat.isLocationEnabled(locatioManager)
            else
                false
        }
    }

    init {
        context = activity.applicationContext
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        if (inventLocationListener != null) {
                            Log.i(
                                TAG,
                                "continuouslyLocation ---> " + location.latitude + " , " + location.longitude
                            )
                            inventLocationListener.onContinuouslyLocation(location)
                        }
                    }
                }
            }
        }
    }

    fun getLastLocation(param: String) {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient!!.lastLocation
            .addOnSuccessListener(
                activity
            ) { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    if (inventLocationListener != null) {
                        Log.i(
                            TAG,
                            """
                                getLastLocation ($param) ---> ${location.latitude}
                                ${location.longitude}
                                """.trimIndent()
                        )
                        inventLocationListener.onLastLocationListener(param, location)
                    }
                }
            }
    }

    fun startContinuouslyLocation() {
        Log.i(TAG, "startContinuouslyLocation")

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    fun stopContinuouslyLocation() {
        if (locationCallback != null) {
            Log.i(TAG, "stopContinuouslyLocation")
            fusedLocationClient?.removeLocationUpdates(locationCallback)
        }
    }

    fun getAddressFromLocation(
        param: String,
        location: Location
    ) {
        try {
            val geocoder = Geocoder(context, Locale("id", "ID"))
            val addresses =
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    5
                ) // Here 5 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size > 0) {
                val result = addresses[0]
                val builder = StringBuilder()
                val addressElements: MutableList<String?> =
                    ArrayList()
                for (i in 0..result.maxAddressLineIndex) {
                    addressElements.add(result.getAddressLine(i))
                }
                builder.append(TextUtils.join(", ", addressElements))
                if (inventLocationListener != null) {
                    Log.i(
                        TAG,
                        "getAddressFromLocation ($param) ---> $builder"
                    )
                    inventLocationListener.onParseLocationToAddress(param, builder.toString())
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getLocationFromAddress(param: String, address: String?) {
        try {
            val coder = Geocoder(context, Locale("id", "ID"))
            val result =
                coder.getFromLocationName(address, 5)
            if (inventLocationListener != null) {
                if (result.size > 0) {
                    val location = result[0]
                    val locationResult = Location("location_provider")
                    locationResult.latitude = location.latitude
                    locationResult.longitude = location.longitude
                    Log.i(
                        TAG,
                        """
                            getLocationFromAddress ($param) ---> ${locationResult.latitude}
                            ${locationResult.longitude}
                            """.trimIndent()
                    )
                    inventLocationListener.onParseAddressToLocation(param, locationResult)
                } else {
                    inventLocationListener.onParseAddressToLocation(param, null)
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun turnOnGPS(param: String) {
        if (isLocationServiceEnabled(activity.applicationContext))
            inventLocationListener?.onGPSEnable(param, true)
        else {
            val mSettingsClient = LocationServices.getSettingsClient(context!!)

            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 10 * 1000.toLong()
            locationRequest.fastestInterval = 2 * 1000.toLong()
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            val mLocationSettingsRequest = builder.build()

            //**************************

            //**************************
            builder.setAlwaysShow(true) //this is the key ingredient

            //**************************

            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener {
                    inventLocationListener?.onGPSEnable(param, true)
                }
                .addOnFailureListener {
                    when ((it as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = it as ResolvableApiException
                                rae.startResolutionForResult(activity, REQUEST_ENABLE_GPS)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        }
    }
}