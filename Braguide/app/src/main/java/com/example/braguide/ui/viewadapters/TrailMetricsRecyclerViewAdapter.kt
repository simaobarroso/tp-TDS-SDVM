package com.example.braguide.ui.viewadapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.Trail
import com.example.braguide.model.TrailMetrics
import com.squareup.picasso.Picasso
import com.example.braguide.R
import com.example.braguide.ui.MainActivity

class TrailMetricsRecyclerViewAdapter(
    private val metrics: List<TrailMetrics>,
    private val trails: List<Trail>
) :
    RecyclerView.Adapter<TrailMetricsRecyclerViewAdapter.ViewHolder>() {
    private var listener: ClickListener? = null

    interface ClickListener {
        fun onItemClick(metrics: TrailMetrics?)
    }

    fun setClickListener(listener: ClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_trail_metrics_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = trails[position]
        holder.trailName.text = trails[position].trailName
        val durationText: String = trails[position].trailDuration + " minutes"
        holder.duration.text = durationText
        Picasso.get().load(
            trails[position].imageUrl.replace("http", "https")
        )
            .into(holder.imageView)
        holder.timeUsed.text = metrics[position].timeTaken.toString() + " seconds"
        holder.percentage.text = metrics[position].completedPercentage.toString()

        // Set text color based on theme mode
        val textColor: Int
        val cardColor: Int
        val mainActivity = holder.itemView.context as MainActivity
        if (mainActivity.isDarkModeEnabled) {
            textColor = Color.WHITE
            cardColor = Color.BLACK
        } else {
            textColor = Color.BLACK
            cardColor = Color.WHITE
        }
        holder.trailName.setTextColor(textColor)
        holder.duration.setTextColor(textColor)
        holder.timeUsed.setTextColor(textColor)
        holder.percentage.setTextColor(textColor)
        val cd = holder.mView.findViewById<CardView>(R.id.card_view)
        cd.setCardBackgroundColor(cardColor)
        // Set click listener for each item
        holder.itemView.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onItemClick(metrics[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return trails.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val trailName: TextView = mView.findViewById<TextView>(R.id.trailTitle)
        val duration: TextView = mView.findViewById<TextView>(R.id.trailTime)
        val imageView: ImageView = mView.findViewById<ImageView>(R.id.cardimage)
        val percentage: TextView = mView.findViewById<TextView>(R.id.trail_metricsCompletePercentage)
        val timeUsed: TextView = mView.findViewById<TextView>(R.id.trail_metricsTImeTaken)
        var mItem: Trail? = null

        override fun toString(): String {
            return super.toString() + trailName + duration
        }
    }
}