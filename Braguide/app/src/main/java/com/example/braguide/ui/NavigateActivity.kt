package com.example.braguide.ui


import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.braguide.R
import com.example.braguide.model.EdgeTip
import com.example.braguide.model.Servico
import com.example.braguide.model.Trail
import com.example.braguide.model.Trip
import com.example.braguide.model.User
import com.example.braguide.ui.fragments.MapsFragment
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.viewModel.TrailsViewModelFactory
import com.example.braguide.viewModel.UserViewModel
import com.example.braguide.viewModel.UserViewModelFactory
import com.squareup.picasso.Picasso
import java.io.IOException
import java.io.Serializable
import java.util.stream.Collectors
import kotlin.RuntimeException
import kotlin.arrayOf



class NavigateActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //do nothing
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        setContentView(R.layout.activity_navigation)
        if (intent != null) {
            if (intent.hasExtra("trip")) {
                val trip = intent.serializable<Trip>("trip")
                load(trip!!.trail)
            } else if (intent.hasExtra("id")) {
                val trailId = intent.getIntExtra("id", 0)
                Log.d("TRAIL", "$trailId")


                val viewModelFactory = TrailsViewModelFactory(application)
                val trailViewModel = ViewModelProvider(this, viewModelFactory)[TrailsViewModel::class.java]

                val userModelFactory = UserViewModelFactory(application)
                val userViewModel = ViewModelProvider(this, userModelFactory)[UserViewModel::class.java]


                val trailLiveData: LiveData<Trail> = trailViewModel.getTrailById(trailId)

                trailLiveData.observe(this) { trail: Trail? ->
                    if (trail != null) {
                        try {
                            Log.d("TRAIL", "$trail")
                            val userLiveData: LiveData<User> =
                                userViewModel.user
                            userLiveData.observe(this
                            ) { user: User? ->
                                if (user != null) {
                                    val trip: Trip = Trip(trail, user.username)
                                    val serviceIntent: Intent =
                                        Intent(
                                            this,
                                            Servico::class.java
                                        )
                                    serviceIntent.putExtra("trip", trip)
                                    startForegroundService(serviceIntent)
                                    startNavigation(trail)
                                    load(trail)
                                    userLiveData.removeObservers(this)
                                }
                            }
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                        trailLiveData.removeObservers(this)
                    }
                }
            }
        }
    }

    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    private fun load(trail: Trail) {
        val titulo = findViewById<TextView>(R.id.trail_description_title)
        Log.d("TRAIL", trail.trailName)
        titulo.text = trail.trailName
        val imagem = findViewById<ImageView>(R.id.trailImageDescription)
        Picasso.get().load(trail.imageUrl.replace("http", "https"))
            .error(R.drawable.no_preview_image)
            .into(imagem)
        val stop = findViewById<Button>(R.id.start_trip_button)
        stop.setOnClickListener { v: View? -> endNavigation() }
        val fragmentManager = supportFragmentManager
        val mapsFragment: MapsFragment = MapsFragment.newInstance(trail.getRoute())
        val transaction2 = fragmentManager.beginTransaction()
        transaction2.replace(R.id.maps_trail_overview, mapsFragment)
        transaction2.commit()
    }

    private fun startNavigation(trail: Trail) {
        // Create a new instance of the destination fragment
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PackageManager.PERMISSION_GRANTED
        )
        if (MapsFragment.meetsPreRequisites(this)) {
            val route = trail.getRoute().stream()
                .map { obj: EdgeTip -> obj.getLocationString() }
                .collect(Collectors.toList())
            val lastIndex = route.size - 1
            val destination = route[lastIndex]
            val waypoints = route.subList(0, lastIndex).joinToString(separator = "|")
            val mapIntentUri = Uri.parse(
                "google.navigation:q=" + destination
                        + Uri.decode("&waypoints=$waypoints")
            )
            val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(mapIntent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Install Google Maps to navigate", Toast.LENGTH_LONG).show()
            }
        } else {
            val toast = Toast.makeText(this, "Install Google Maps to navigate", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private fun endNavigation() {
        val serviceIntent = Intent(this, Servico::class.java)
        stopService(serviceIntent)
        Toast.makeText(this, "Registed metrics in the history", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    companion object {
        fun newInstance(): NavigateActivity {
            return NavigateActivity()
        }
    }
}