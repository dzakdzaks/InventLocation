package com.dzakdzaks.iLocation

import android.location.Location

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 17 August 2020 at 2:18 PM.
 * Project Name => Bunda Mart Customer
 * Package Name => com.invent.bmcustomer.utils.helper
 * ==================================//==================================
 * ==================================//==================================
 */
abstract class AbstractInventLocation : InventLocationListener {
    override fun onLastLocationListener(param: String?, location: Location?) {}
    override fun onContinuouslyLocation(location: Location?) {}
    override fun onParseLocationToAddress(param: String?, address: String?) {}
    override fun onParseAddressToLocation(param: String?, location: Location?) {}
    override fun onGPSEnable(param: String?, isEnable: Boolean) {}
}
