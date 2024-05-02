package com.example.braguide.ui.fragments

import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.Contact
import java.io.IOException
import androidx.fragment.app.Fragment
import com.example.braguide.R
import com.example.braguide.viewModel.AppDataViewModel
import com.example.braguide.viewModel.AppDataViewModelFactory
import com.example.braguide.ui.viewadapters.ContactsRecyclerViewAdapter


class ContactsFragment
    : Fragment() {
    private var mColumnCount = 1
    private var appViewModel: AppDataViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mColumnCount = requireArguments().getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_contacts_list, container, false)
        val appModelFactory = AppDataViewModelFactory(requireActivity().application)
        appViewModel = ViewModelProvider(this, appModelFactory)[AppDataViewModel::class.java]
        try {
            appViewModel!!.appData.observe(getViewLifecycleOwner()) { appInfo ->
                val contacts: List<Contact> = appInfo.contacts
                Log.e("Contact List", "contacts size:" + contacts.size)
                loadRecyclerView(view, contacts)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return view
    }

    private fun loadRecyclerView(view: View, contacts: List<Contact>) {
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(LinearLayoutManager(context))
            } else {
                recyclerView.setLayoutManager(GridLayoutManager(context, mColumnCount))
            }
            val adapter = ContactsRecyclerViewAdapter(contacts)
            recyclerView.setAdapter(adapter)
            if (isTelephonyEnabled) {
                adapter.setOnPhoneClickListener(object : ContactsRecyclerViewAdapter.OnPhoneClickListener {
                    override fun onPhoneClick(phoneNumber: String?) {
                        if (phoneNumber != null) {
                            callPhoneNumber(phoneNumber)
                        }
                    }
                })
            }
        }
    }

    private val isTelephonyEnabled: Boolean
        get() {
            val tm = context?.getSystemService(TELEPHONY_SERVICE) as TelephonyManager?
            return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY
        }

    private fun callPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.setData(Uri.parse("tel:$phoneNumber"))
        context?.startActivity(intent)
    }

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCount: Int): ContactsFragment {
            val fragment = ContactsFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.setArguments(args)
            return fragment
        }
    }
}