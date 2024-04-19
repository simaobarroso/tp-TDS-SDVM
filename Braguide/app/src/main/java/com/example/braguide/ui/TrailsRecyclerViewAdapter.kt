package com.example.braguide.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.Trail
import com.squareup.picasso.Picasso

import com.example.braguide.R

class TrailsRecyclerViewAdapter(private val mValues: List<Trail>) :
    RecyclerView.Adapter<TrailsRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mIdView.text = mValues[position].id
        Picasso.get()
            .load(mValues[position].imageUrl.replace("http:", "https:"))
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView
        val imageView: ImageView

        init {
            mIdView = mView.findViewById<TextView>(R.id.item_number)
            imageView = mView.findViewById<ImageView>(R.id.cardimage)
        }

        override fun toString(): String {
            return super.toString() + mIdView
        }
    }
}