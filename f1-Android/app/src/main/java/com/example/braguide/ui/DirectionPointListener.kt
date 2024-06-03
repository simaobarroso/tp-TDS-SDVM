package com.example.braguide.ui

import com.google.android.gms.maps.model.PolylineOptions


fun interface DirectionPointListener {
    fun onPath(polyLine: PolylineOptions)
}