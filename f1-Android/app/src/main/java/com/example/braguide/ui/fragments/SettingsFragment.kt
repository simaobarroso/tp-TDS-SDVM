package com.example.braguide.ui.fragments


import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.res.Configuration
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.braguide.R

import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task


class SettingsFragment : Fragment() {
    private var locationSwitch: SwitchCompat? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        handleLocationSwitch(view)
        handleDarkModeSwitch(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        if (locationSwitch != null) {
            val locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationSwitch!!.setChecked(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun handleLocationSwitch(view: View) {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isLocationEnabled // Use isLocationEnabled instead of isProviderEnabled
        val localization_switch = view.findViewById<SwitchCompat>(R.id.localization_switch)
        locationSwitch = localization_switch
        locationSwitch!!.isChecked = isGPSEnabled // Use isChecked instead of setChecked

        locationSwitch!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            // Handle the switch state change here
            if (isChecked) {
                val locationRequest: LocationRequest = LocationRequest.create()


                val locationSettingsRequestBuilder: LocationSettingsRequest.Builder =
                    LocationSettingsRequest.Builder()
                locationSettingsRequestBuilder.addLocationRequest(locationRequest)
                locationSettingsRequestBuilder.setAlwaysShow(true)

                val settingsClient: SettingsClient =
                    LocationServices.getSettingsClient(requireActivity())
                val task: Task<LocationSettingsResponse> =
                    settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build())
                task.addOnFailureListener(requireActivity()) { e ->
                    if (e is ResolvableApiException) {
                        try {
                            val resolvableApiException: ResolvableApiException =
                                e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                requireActivity(),
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (sendIntentException: IntentSender.SendIntentException) { // Corrected import for SendIntentException
                            sendIntentException.printStackTrace()
                        }
                    }
                }
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) // Use Settings.ACTION_LOCATION_SOURCE_SETTINGS instead of Intent(ACTION_LOCATION_SOURCE_SETTINGS)
            }
        }
    }

    fun handleDarkModeSwitch(view: View) {
        val sharedPrefs =
            requireActivity().getSharedPreferences("BraguidePreferences", Context.MODE_PRIVATE)
        val dark_mode_switch = view.findViewById<SwitchCompat>(R.id.dark_mode_switch)
        val isDarkModeEnabled = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                === Configuration.UI_MODE_NIGHT_YES)
        dark_mode_switch.setChecked(isDarkModeEnabled)
        dark_mode_switch.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            // Handle the switch state change here
            if (isChecked) {
                val editor = sharedPrefs.edit()
                editor.putBoolean("darkModeSwitchState", true)
                editor.apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Toast.makeText(context, "Dark mode enabled", Toast.LENGTH_SHORT).show()
            } else {
                val editor = sharedPrefs.edit()
                editor.putBoolean("darkModeSwitchState", false)
                editor.apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Toast.makeText(requireActivity(), "Dark mode disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 0x1
    }
}