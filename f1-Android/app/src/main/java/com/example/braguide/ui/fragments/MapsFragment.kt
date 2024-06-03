package com.example.braguide.ui.fragments



import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.braguide.R
import com.example.braguide.model.EdgeTip
import com.example.braguide.ui.DirectionPointListener
import com.example.braguide.ui.GetPathFromLocation
import com.example.braguide.viewModel.TrailsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class MapsFragment : Fragment {
    private lateinit var mMap: GoogleMap
    val edgeTips: List<EdgeTip>

    constructor(edgeTips: List<EdgeTip>) {
        this.edgeTips = edgeTips
    }

    constructor() {
        edgeTips = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_maps_overview, container, false)
        val mapFragment = getChildFragmentManager()
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap: GoogleMap ->
            googleMap.uiSettings.isZoomControlsEnabled = true
            loadMap(googleMap)
        }
        return view
    }

    fun loadMap(googleMap: GoogleMap) {
        mMap = googleMap
        if (edgeTips.size > 0) {
            val wayPointsAPI = ArrayList<LatLng>()
            for (edgeTip in edgeTips) {
                wayPointsAPI.add(edgeTip.getMapsCoordinate())
                mMap.addMarker(
                    MarkerOptions().position(edgeTip.getMapsCoordinate())
                        .title(edgeTip.pinName)
                )
            }
            val source = wayPointsAPI[0]
            if (wayPointsAPI.size >= 2) {
                val destination = wayPointsAPI[wayPointsAPI.size - 1]
                wayPointsAPI.removeAt(0)
                wayPointsAPI.removeAt(wayPointsAPI.size - 1)
                GetPathFromLocation(
                    requireActivity(), source, destination, wayPointsAPI, mMap, false, false
                ) { polyLine: PolylineOptions ->
                    polyLine.color(R.color.teal_200)
                    mMap.addPolyline(polyLine)
                }.execute()
                mMap.uiSettings.isZoomControlsEnabled = true
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 12f))
            }
        }
    }



    companion object {
        fun newInstance(edgeTips: List<EdgeTip>): MapsFragment {
            return MapsFragment(edgeTips)
        }

        fun meetsPreRequisites(context: Context): Boolean {
            val pm = context.packageManager
            return isPackageInstalled("com.google.android.apps.maps", pm)
        }

        private fun isPackageInstalled(
            packageName: String,
            packageManager: PackageManager
        ): Boolean {
            return try {
                packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    }
}