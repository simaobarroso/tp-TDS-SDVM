package com.example.braguide.ui.viewadapters


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.Edge
import com.example.braguide.model.EdgeTip
import com.example.braguide.ui.viewadapters.EdgeTipViewAdapter.setImageView
import com.example.braguide.R
import com.example.braguide.ui.MainActivity


class EdgesRecyclerViewAdapter(private val mValues: List<Edge>, private val c: Context) :
    RecyclerView.Adapter<EdgesRecyclerViewAdapter.ViewHolder>() {
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(ed: Edge?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_edge_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.edge_transport.text = mValues[position].edgeTransport
        holder.edge_time.text = "${mValues[position].edgeDuration} minutes"
        holder.edge_road_type.text = mValues[position].edgeDesc
        holder.pin_name_origem.text = mValues[position].edgeStart.pinName
        if (!setImageView(mValues[position].edgeStart, holder.imageView2_origem)) {
            holder.imageView2_origem.setImageResource(R.drawable.no_preview_image)
        }
        holder.pin_name_destino.text = mValues[position].edgeEnd.pinName
        if (!setImageView(mValues[position].edgeEnd, holder.imageView2_destino)) {
            holder.imageView2_destino.setImageResource(R.drawable.no_preview_image)
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
        val cd1 = holder.mView.findViewById<CardView>(R.id.card_pin_view_origem)
        val cd2 = holder.mView.findViewById<CardView>(R.id.card_pin_view_destino)
        val cd3 = holder.mView.findViewById<CardView>(R.id.card_view)
        val cd4 = holder.mView.findViewById<CardView>(R.id.outer_card)
        cd1.setCardBackgroundColor(cardColor)
        cd2.setCardBackgroundColor(cardColor)
        cd3.setCardBackgroundColor(cardColor)
        cd4.setCardBackgroundColor(cardColor)
        holder.edge_transport.setTextColor(textColor)
        holder.edge_time.setTextColor(textColor)
        holder.edge_road_type.setTextColor(textColor)
        holder.pin_name_origem.setTextColor(textColor)
        holder.pin_name_destino.setTextColor(textColor)
        holder.card_origem.setOnClickListener { e: View? ->
            replaceFragment(
                mValues[position].edgeStart
            )
        }
        holder.card_destino.setOnClickListener { e: View? ->
            replaceFragment(
                mValues[position].edgeEnd
            )
        }
    }

    private fun replaceFragment(ed: EdgeTip) {
        val bundle = Bundle()
        bundle.putInt("id", ed.id)
        val navHostFragment =
            (c as FragmentActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navHostFragment!!.navController.navigate(R.id.pinFragment, bundle)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val edge_transport: TextView
        val edge_time: TextView
        val edge_road_type: TextView
        val pin_name_origem: TextView
        val pin_name_destino: TextView
        val imageView2_origem: ImageView
        val imageView2_destino: ImageView
        val card_origem: CardView
        val card_destino: CardView
        var mItem: Edge? = null

        init {
            card_origem = mView.findViewById<CardView>(R.id.card_pin_view_origem)
            card_destino = mView.findViewById<CardView>(R.id.card_pin_view_destino)
            pin_name_origem = mView.findViewById<TextView>(R.id.pin_name_origem)
            imageView2_origem = mView.findViewById<ImageView>(R.id.imageView2_origem)
            pin_name_destino = mView.findViewById<TextView>(R.id.pin_name_destino)
            imageView2_destino = mView.findViewById<ImageView>(R.id.imageView2_destino)
            edge_transport = mView.findViewById<TextView>(R.id.edge_transport)
            edge_time = mView.findViewById<TextView>(R.id.edge_time)
            edge_road_type = mView.findViewById<TextView>(R.id.edge_road_type)
        }

        override fun toString(): String {
            return super.toString()
        }
    }
}