package com.example.braguide.ui.viewadapters


import android.graphics.Color
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.Contact
import com.example.braguide.R
import com.example.braguide.ui.MainActivity


class ContactsRecyclerViewAdapter(private val mValues: List<Contact>) :
    RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>() {
    private var listener: OnPhoneClickListener? = null
    fun setOnPhoneClickListener(listener: OnPhoneClickListener?) {
        this.listener = listener
    }

    interface OnPhoneClickListener {
        fun onPhoneClick(phoneNumber: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contact_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.contact_name.text = mValues[position].contactName
        holder.contact_phone.text = mValues[position].contactPhone
        holder.contact_mail.text = mValues[position].contactMail
        holder.contact_url.text = mValues[position].contactUrl

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
        val cd = holder.mView.findViewById<CardView>(R.id.contact_card_view)
        cd.setCardBackgroundColor(cardColor)
        holder.contact_name.setTextColor(textColor)
        holder.contact_phone.setTextColor(textColor)
        holder.contact_mail.setTextColor(textColor)
        holder.contact_url.setTextColor(textColor)

        // Set click listener for phone
        holder.contact_phone.setOnClickListener { v: View? ->
            if (listener != null) {
                val phoneNumber = mValues[position].contactPhone
                if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
                    listener!!.onPhoneClick(phoneNumber)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val contact_name: TextView = mView.findViewById<TextView>(R.id.contact_name)
        val contact_phone: TextView = mView.findViewById<TextView>(R.id.contact_phone)
        val contact_url: TextView = mView.findViewById<TextView>(R.id.contact_url)
        val contact_mail: TextView = mView.findViewById<TextView>(R.id.contact_mail)
        var mItem: Contact? = null

        override fun toString(): String {
            return super.toString() + contact_name + contact_phone + contact_url + contact_mail
        }
    }
}