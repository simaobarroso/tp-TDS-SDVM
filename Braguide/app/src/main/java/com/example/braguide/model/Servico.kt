package com.example.braguide.model

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.braguide.repositories.UserRepository
import com.example.braguide.ui.NavigateActivity
import com.example.braguide.ui.NotificationPinScreenActivity
import java.io.Serializable


//import com.example.braguide.ui.NotificationPinScreenActivity


class Servico : Service() {
    private var mLocationManager: LocationManager? = null
    private var trip: Trip? = null

    inner class LocationListener(provider: String) : android.location.LocationListener {
        var mLastLocation: Location

        init {
            Log.e(TAG, "LocationListener $provider")
            mLastLocation = Location(provider)
        }

        override fun onLocationChanged(location: Location) {
            Log.e(TAG, "onLocationChanged: $location")
            val edgeTip = trip!!.verifyPins(location)
            edgeTip?.let { createNotification(it) }
            mLastLocation.set(location)
        }

        override fun onProviderDisabled(provider: String) {
            Log.e(TAG, "onProviderDisabled: $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.e(TAG, "onProviderEnabled: $provider")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Log.e(TAG, "onStatusChanged: $provider")
        }
    }

    var mLocationListeners = arrayOf<LocationListener>(
        LocationListener(LocationManager.GPS_PROVIDER),
        LocationListener(LocationManager.NETWORK_PROVIDER)
    )

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        trip = intent.serializable<Trip>("trip")
        createNotificationChannel()
        val notificationIntent = Intent(
            this,
            NavigateActivity::class.java
        )
        notificationIntent.putExtra("trip", trip)

        // Generate a unique request code using current timestamp
        val timestamp = System.currentTimeMillis()
        val requestCode = timestamp.toInt()
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Trip started")
            .setContentText(trip!!.trail.trailName)
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        startForeground(1, notification)
        Log.e(TAG, "Trail name:" + trip!!.trail.trailName)
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")
        initializeLocationManager()
        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                mLocationListeners[1]
            )
        } catch (ex: SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "network provider does not exist, " + ex.message)
        }
        try {
            mLocationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, LOCATION_INTERVAL.toLong(), LOCATION_DISTANCE,
                mLocationListeners[0]
            )
        } catch (ex: SecurityException) {
            Log.i(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException) {
            Log.d(TAG, "gps provider does not exist " + ex.message)
        }
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        super.onDestroy()
        val repository = UserRepository(application)
        trip?.let { repository.addTrailMetrics(it) }
        Log.e(TAG, "Metrics have been added")
        if (mLocationManager != null) {
            for (mLocationListener in mLocationListeners) {
                try {
                    mLocationManager!!.removeUpdates(mLocationListener)
                } catch (ex: Exception) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex)
                }
            }
        }
    }

    fun createNotification(edgeTip: EdgeTip) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("channel_id", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(
            applicationContext, "channel_id"
        )
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentTitle(edgeTip.pinName)
            .setContentText(edgeTip.pinDesc)

        // Add action button
        val intent = Intent(
            applicationContext,
            NotificationPinScreenActivity::class.java
        )
        intent.putExtra("EdgeTip", edgeTip)
        val stackBuilder = TaskStackBuilder.create(
            applicationContext
        )
        stackBuilder.addNextIntentWithParentStack(intent)
        // Get the PendingIntent containing the entire back stack
        val resultPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(resultPendingIntent)
        val notification = builder.build()
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notificationManagerCompat = NotificationManagerCompat.from(
                applicationContext
            )
            notificationManagerCompat.notify(2, notification)
        }
    }

    private fun initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager")
        if (mLocationManager == null) {
            mLocationManager =
                applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        private const val TAG = "SERVIVETESTGPS"
        private const val LOCATION_INTERVAL = 1000
        private const val LOCATION_DISTANCE = 1f
    }
}