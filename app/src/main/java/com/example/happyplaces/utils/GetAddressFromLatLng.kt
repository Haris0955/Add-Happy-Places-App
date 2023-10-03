package com.example.happyplaces.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import java.util.*

class GetAddressFromLatLng(context: Context, private val lat: Double, private val lng: Double) {
    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())

    //to decode the lat and lng value to text address
    private lateinit var mAddressListener: AddressListener

/**  This is a suspend function used to initiate the process of fetching the address from latitude
 * and longitude in the background. It calls the getAddress function and then switches to the main
 * thread using withContext(Main) to handle UI-related updates based on the obtained address or an error. **/
    suspend fun launchBackgroundProcessForRequest() {
        val address = getAddress()

        withContext(Main) {
            //switch to Main thread, cuz we're going to update the UI related values from here on
            // if we get a valid address
            if (address.isEmpty()) {
                mAddressListener.onError()
            } else {
                mAddressListener.onAddressFound(address)  //updating UI
            }
        }
    }

/** This private suspend function performs the actual address retrieval process. It uses the
 * Geocoder to get a list of addresses associated with the provided latitude and longitude. It then
 * extracts the first (most relevant) address from the list and constructs a readable address string
 * from its address lines.**/
    private suspend fun getAddress(): String {
        try {
            //there may be multiple locations/places associated with the lat and lng, we take the top/most relevant address
            val addressList: List<Address>? = geocoder.getFromLocation(lat, lng, 1)

            if (!addressList.isNullOrEmpty()) {
                val address: Address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    //Returns the largest index currently in use to specify an address line.
                    sb.append(address.getAddressLine(i) + " ")
                }
                //to remove the last " "
                sb.deleteCharAt(sb.length - 1)

                return sb.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

/**This function allows you to set an AddressListener instance, which can receive callbacks when
an address is found or when an error occurs during address retrieval.**/
    fun setCustomAddressListener(addressListener: AddressListener) {
        //to attach the listener to the class property
        this.mAddressListener = addressListener
    }

    //can be defined anywhere
    interface AddressListener {
        fun onAddressFound(address: String?)
        fun onError()
    }
}