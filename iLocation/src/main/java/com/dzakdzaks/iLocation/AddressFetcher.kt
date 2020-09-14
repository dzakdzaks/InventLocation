package com.dzakdzaks.iLocation

import android.location.Address
import android.location.Location
import com.google.android.gms.maps.model.LatLng

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Wednesday, 02 September 2020 at 9:34 AM.
 * Project Name => Bunda Mart Customer
 * Package Name => com.invent.bmcustomer.utils.invent_location
 * ==================================//==================================
 * ==================================//==================================
 */
interface AddressFetcher {
    /**
     * Fetch address from location
     */
    fun fetchAddressFromLocation(location: Location): Address?

    /**
     * Fetch address from lat lng
     */
    fun fetchAddressFromLatLng(latLng: LatLng): Address?

    /**
     * Fetch address string from location
     */
    
    fun fetchAddressStringFromLocation(location: Location): String?

    /**
     * Fetch address string from lat lng
     */
    
    fun fetchAddressStringFromLatLng(latLng: LatLng): String?

    
    fun fetchKecamatanFromLatlng(latLng: LatLng): String?

    fun fetchKelurahanFromLatlng(latLng: LatLng): String?
    
    fun fetchCityFromLatlng(latLng: LatLng): String?

    
    fun fetchPropinsiLatlng(latLng: LatLng): String?

    
    fun fetchPostalCode(latLng: LatLng): String?

    
    fun fetchCityFromLocation(latLng: Location): String?

    
    fun fetchPropinsiLocation(latLng: Location): String?

    
    fun fetchKecamatanFromLocation(latLng: Location): String?

    fun fetchKelurahanFromLatlng(latLng: Location): String?
}
