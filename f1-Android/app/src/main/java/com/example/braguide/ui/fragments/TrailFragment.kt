package com.example.braguide.ui.fragments



import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.braguide.R
import com.example.braguide.model.Trail
import com.example.braguide.ui.NavigateActivity
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.viewModel.TrailsViewModelFactory
import com.squareup.picasso.Picasso



class TrailFragment : Fragment() {
    private var trailViewModel: TrailsViewModel? = null
    private var id : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = requireArguments().getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_trail_description, container, false)
        val viewModelFactory = TrailsViewModelFactory(requireActivity().application)
        trailViewModel = ViewModelProvider(this, viewModelFactory)[TrailsViewModel::class.java]

        trailViewModel!!.getTrailById(id).observe(viewLifecycleOwner) { x ->
            loadView(view, x)
        }


        return view
    }

    private fun loadView(view: View, trail: Trail) {

        val title = view.findViewById<TextView>(R.id.trail_description_title)
        title.text = trail.trailName
        val image = view.findViewById<ImageView>(R.id.trailImageDescription)
        Picasso.get().load(
            trail.imageUrl
                .replace("http", "https")
        )
            .into(image)
        val intro = view.findViewById<Button>(R.id.start_trip_button)
        intro.setOnClickListener { v: View? ->
            if (from(requireContext())
                    .areNotificationsEnabled()
            ) {
                val intent: Intent = Intent(
                    requireActivity(),
                    NavigateActivity::class.java
                )
                intent.putExtra("id", trail.id) // add a string extra
                startActivity(intent)
            } else {
                // Create an intent to open app notification settings
                val settingsIntent = Intent()
                settingsIntent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                settingsIntent.putExtra(
                    Settings.EXTRA_APP_PACKAGE,
                    requireContext().packageName
                )

                // Check if the intent can be resolved
                if (settingsIntent.resolveActivity(
                        requireContext().packageManager
                    ) != null
                ) {
                    startActivity(settingsIntent)
                } else {
                    // Provide an alternative action or message if the intent cannot be resolved
                    Toast.makeText(
                        requireContext(),
                        "Please enable notifications in your device settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val childFragmentManager: FragmentManager = getChildFragmentManager()
        val childFragment: EdgeListFragment = EdgeListFragment.newInstanceByTrails(
            listOf(id)
        )
        val transaction1: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction1.add(R.id.pin_list_content, childFragment)
        transaction1.commit()


        val mapsFragment = MapsFragment.newInstance(trail.getRoute())
        val transaction2: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction2.replace(R.id.maps_trail_overview, mapsFragment)
        transaction2.commit()


    }
}