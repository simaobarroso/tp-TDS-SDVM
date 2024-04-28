package com.example.braguide.ui


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.R
import com.example.braguide.model.Trail
import com.squareup.picasso.Picasso
import java.util.Locale


class TrailsRecyclerViewAdapter(private val mValues: List<Trail>) :
    RecyclerView.Adapter<TrailsRecyclerViewAdapter.ViewHolder>() {

    private lateinit var listener : OnItemClickListener

    private var filteredTrails: MutableList<Trail>? = mutableListOf()


    interface OnItemClickListener {
        fun onItemClick(trail: Trail?)
    }

    fun setListener(listener: OnItemClickListener?) {
        this.listener = listener!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mIdView.text = mValues[position].id
        holder.mItem = mValues[position]
        Picasso.get()
            .load(mValues[position].imageUrl.replace("http:", "https:"))
            .into(holder.imageView)

        val cd: CardView = holder.mView.findViewById(R.id.card_trail_main_view)
        cd.setCardBackgroundColor(Color.WHITE)

        holder.itemView.setOnClickListener {
            listener.onItemClick(mValues[position])
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.findViewById(R.id.trail_main_name)
        val imageView: ImageView = mView.findViewById(R.id.trail_main_image)
        lateinit var mItem : Trail

        override fun toString(): String {
            return super.toString() + mIdView
        }
    }


    fun filterData(query: String) {
        filteredTrails?.clear()
        for (data in mValues) {
            if (data.trailName.lowercase(Locale.ROOT)
                    .contains(query.lowercase(Locale.ROOT))
            ) {
                filteredTrails?.add(data)
            }
        }
        notifyDataSetChanged()
    }

    fun reset() {
        filteredTrails?.clear()
        filteredTrails?.addAll(mValues)
        notifyDataSetChanged()
    }
}