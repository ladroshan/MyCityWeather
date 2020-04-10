package com.mycityweather.locationHelper

import android.app.Activity
import android.location.Location
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*

class MyLocation (activity: Activity, mylocationCallBack: MyLocationCallBack) {

    private val TAG = "location"
    private var mTrackingLocation: Boolean = true
    private var mLocationCallback: LocationCallback? = null


    private var mFusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            activity
        )


    interface MyLocationCallBack {

        fun permissionDenied()

        fun locationSettingFailed()

        fun getLocation(location: Location)

    }


    /**
     * Sets up the location request.
     *
     * @return The LocationRequest object containing the desired parameters.
     */
    private val locationRequest: LocationRequest
        get() {
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 5000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            return locationRequest
        }

    init {

        // Initialize the location callbacks.
        mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {


                locationResult?.lastLocation?.let { mylocationCallBack.getLocation(it) }

            }
        }


        var permissionHelper = (activity as FragmentActivity).supportFragmentManager
            .findFragmentByTag(TAG) as PermissionHelper?
        if (permissionHelper == null) {
            permissionHelper =
                PermissionHelper.newInstance(object : PermissionHelper.PermissionListener {


                    override fun fetchLocation() {
                        mTrackingLocation = true
                        mFusedLocationClient.requestLocationUpdates(
                            locationRequest,
                            mLocationCallback!!, null
                        )


                    }

                    override fun permissionStatus(permissionValue: Boolean) {

                        if (permissionValue)
                            fetchLocation()
                        else
                            mylocationCallBack.permissionDenied()


                    }

                    override fun settingStatus(settingValue: Boolean) {


                        if (settingValue)
                            permissionHelper?.getPermissionStatus()
                        else
                            mylocationCallBack.locationSettingFailed()


                    }

                    override fun stopLocationUpdates() {

                        mFusedLocationClient.removeLocationUpdates(mLocationCallback)

                    }


                })

            activity.supportFragmentManager.beginTransaction().add(permissionHelper, TAG)
                .commit()

            permissionHelper.setLocationRequest(locationRequest)


        }
    }


}