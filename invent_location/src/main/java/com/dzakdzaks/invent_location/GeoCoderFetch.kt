package com.dzakdzaks.invent_location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.util.*

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Wednesday, 02 September 2020 at 9:34 AM.
 * Project Name => Bunda Mart Customer
 * Package Name => com.invent.bmcustomer.utils.invent_location
 * ==================================//==================================
 * ==================================//==================================
 */
class GeoCoderFetch(context: Context?) : AddressFetcher {

    private val geoCoder: Geocoder

    override fun fetchAddressFromLocation(location: Location): Address? {
        val latLng = convertLocationToLatLng(location)
        return geoCoder.getFromLocation(latLng.latitude, latLng.longitude, GEO_CODER_MAX_RESULT)[0]
    }


    override fun fetchAddressFromLatLng(latLng: LatLng): Address? {
        return geoCoder.getFromLocation(latLng.latitude, latLng.longitude, GEO_CODER_MAX_RESULT)[0]
    }


    override fun fetchAddressStringFromLocation(location: Location): String? {
        val address = fetchAddressFromLocation(location)
        return parseAddressStringFromAddress(address!!)
    }


    override fun fetchAddressStringFromLatLng(latLng: LatLng): String? {
        val address = fetchAddressFromLatLng(latLng)
        return parseAddressStringFromAddress(address!!)
    }


    override fun fetchPostalCode(latLng: LatLng): String? {
        val address = fetchAddressFromLatLng(latLng)
        return parsePostalStringFromAddress(address!!)
    }


    override fun fetchCityFromLocation(latLng: Location): String? {
        val address = fetchAddressFromLocation(latLng)
        return parseCityStringFromAddress(address!!)
    }


    override fun fetchPropinsiLocation(latLng: Location): String? {
        val address = fetchAddressFromLocation(latLng)
        return parsePropinsiStringFromAddress(address!!)
    }


    override fun fetchKecamatanFromLocation(latLng: Location): String? {
        val address = fetchAddressFromLocation(latLng)
        return parseKecamatanStringFromAddress(address!!)
    }


    override fun fetchCityFromLatlng(latLng: LatLng): String? {
        val address = fetchAddressFromLatLng(latLng)
        return parseCityStringFromAddress(address!!)
    }


    override fun fetchPropinsiLatlng(latLng: LatLng): String? {
        val address = fetchAddressFromLatLng(latLng)
        return parsePropinsiStringFromAddress(address!!)
    }


    override fun fetchKecamatanFromLatlng(latLng: LatLng): String? {
        val address = fetchAddressFromLatLng(latLng)
        return parseKecamatanStringFromAddress(address!!)
    }

    override fun fetchKelurahanFromLatlng(latLng: LatLng): String? {
        val address = fetchAddressFromLatLng(latLng)
        return parseKelurahanStringFromAddress(address!!)
    }

    override fun fetchKelurahanFromLatlng(latLng: Location): String? {
        val address = fetchAddressFromLocation(latLng)
        return parseKelurahanStringFromAddress(address!!)
    }

    /**
     * Convert location to lat lng
     */
    private fun convertLocationToLatLng(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

    private fun parseAddressStringFromAddress(address: Address): String {
        return address.getAddressLine(0)
    }

    private fun parsePostalStringFromAddress(address: Address): String {
        return address.postalCode ?: "-"
    }

    private fun parseKelurahanStringFromAddress(address: Address): String {
        return address.subLocality ?: "-"
    }

    private fun parseKecamatanStringFromAddress(address: Address): String {
        return address.locality ?: "-"
    }

    private fun parseCityStringFromAddress(address: Address): String {
        return address.subAdminArea ?: "-"
    }

    private fun parsePropinsiStringFromAddress(address: Address): String {
        return address.adminArea ?: "-"
    }

    companion object {
        var LOCALE_LANGUAGE = "id"
        var LOCALE_COUNTRY = "ID"
        var GEO_CODER_MAX_RESULT = 1
    }

    init {
        geoCoder = Geocoder(context, Locale(LOCALE_LANGUAGE, LOCALE_COUNTRY))
    }
}
