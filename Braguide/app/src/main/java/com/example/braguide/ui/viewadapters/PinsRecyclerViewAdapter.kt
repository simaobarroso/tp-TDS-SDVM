package com.example.braguide.ui.viewadapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.EdgeTip
import com.example.braguide.ui.viewadapters.EdgeTipViewAdapter.setImageView
import com.example.braguide.R
import com.example.braguide.ui.MainActivity


class PinsRecyclerViewAdapter(private val mValues: List<EdgeTip>) :
    RecyclerView.Adapter<PinsRecyclerViewAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(ed: EdgeTip?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pin_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.pinName.text = mValues[position].pinName
        if (!setImageView(mValues[position], holder.pinImage)) {
            holder.pinImage.setImageResource(R.drawable.no_preview_image)
        }

        // Set text color based on theme mode
        val textColor: Int
        val cardColor: Int
        val mainActivity = holder.itemView.context as MainActivity
        if (mainActivity.isDarkModeEnabled) {
            textColor = Color.WHITE
            cardColor = Color.GRAY
        } else {
            textColor = Color.BLACK
            cardColor = Color.WHITE
        }
        holder.pinName.setTextColor(textColor)
        val cd = holder.mView.findViewById<CardView>(R.id.card_pin_view)
        cd.setCardBackgroundColor(cardColor)

        // Set click listener for each item
        holder.itemView.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onItemClick(mValues[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val pinName: TextView = mView.findViewById<TextView>(R.id.pin_name)
        val pinImage: ImageView = mView.findViewById<ImageView>(R.id.pin_image)
        var mItem: EdgeTip? = null

        override fun toString(): String {
            return super.toString() + pinName
        }
    }
}