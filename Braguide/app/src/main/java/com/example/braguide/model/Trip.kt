package com.example.braguide.model

import java.io.Serializable
import java.util.stream.Collectors
import java.util.Date
import android.location.Location;
import com.example.braguide.model.EdgeTip
import com.example.braguide.model.Trail

class Trip(val trail: Trail, val username: String) : Serializable{
    val timeStart: Long
    val distance: Long = 0
    private val percorridos: MutableList<EdgeTip>
    private val previstos: MutableList<EdgeTip>

    init {
        timeStart = Date().time
        percorridos = ArrayList()
        previstos = trail.getRoute()
    }

    fun finish(): TrailMetrics {
        val percentageCompletion =
            percorridos.size.toFloat() / (previstos.size + percorridos.size) * 100
        val timeTaken = (Date().time - timeStart).toFloat() / 1000 //returns in seconds
        val p = percorridos.stream().map(EdgeTip::id).collect(Collectors.toList())
        return TrailMetrics(username = username,
            trailId = trail.id,
            completedPercentage =  percentageCompletion,
            timeTaken =  timeTaken,
            visitedPins = p.joinToString(separator = ";"),
            metricId = null)
    }

    fun verifyPins(location: Location): EdgeTip? {
        for (pin in previstos) {
            val distance: Float = pin.getLocation().distanceTo(location)
            if (distance <= 50) {
                percorridos.add(pin)
                previstos.remove(pin)
                return pin
            }
        }
        return null
    }

}