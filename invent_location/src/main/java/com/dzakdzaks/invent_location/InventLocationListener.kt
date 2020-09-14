package com.dzakdzaks.invent_location

import android.location.Location

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 17 August 2020 at 2:17 PM.
 * Project Name => Bunda Mart Customer
 * Package Name => com.invent.bmcustomer.utils.helper
 * ==================================//==================================
 * ==================================//==================================
 */
interface InventLocationListener {
    fun onLastLocationListener(param: String?, location: Location?)
    fun onContinuouslyLocation(location: Location?)
    fun onParseLocationToAddress(param: String?, address: String?)
    fun onParseAddressToLocation(param: String?, location: Location?)
    fun onGPSEnable(param: String?, isEnable: Boolean)
}