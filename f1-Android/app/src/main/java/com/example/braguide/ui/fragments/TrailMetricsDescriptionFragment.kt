package com.example.braguide.ui.fragments


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.braguide.model.Trail
import com.example.braguide.model.TrailMetrics
import com.example.braguide.ui.MainActivity
import com.example.braguide.viewModel.UserViewModel
import com.squareup.picasso.Picasso
import com.example.braguide.R
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.viewModel.TrailsViewModelFactory
import com.example.braguide.viewModel.UserViewModelFactory


class TrailMetricsDescriptionFragment : Fragment() {
    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = requireArguments().getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View =
            inflater.inflate(R.layout.fragment_trail_metrics_description, container, false)
        val viewModelFactory = TrailsViewModelFactory(requireActivity().application)
        val trailViewModel = ViewModelProvider(this, viewModelFactory)[TrailsViewModel::class.java]

        val userModelFactory = UserViewModelFactory(requireActivity().application)
        val userViewModel = ViewModelProvider(this, userModelFactory)[UserViewModel::class.java]
        userViewModel.getMetricsById(id).observe(getViewLifecycleOwner()) { trailMetrics ->
            trailViewModel.getTrailById(trailMetrics.trailId)
                .observe(getViewLifecycleOwner()) { trail ->
                    if (trail != null) {
                        loadView(view, trail, trailMetrics)
                    }
                }
        }
        return view
    }

    private fun loadView(view: View, trail: Trail, metrics: TrailMetrics) {
        val childFragmentManager: FragmentManager = getChildFragmentManager()
        val childFragment: PinListFragment =
            PinListFragment.newInstanceByPins(metrics.getPinIdList())
        val transaction1: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction1.add(R.id.trail_metricspin_list_content, childFragment)
        transaction1.commit()
        val titulo = view.findViewById<TextView>(R.id.trail_metrics_description_title)
        titulo.text = trail.trailName
        val timeTaken = view.findViewById<TextView>(R.id.trail_metricsTImeTaken)
        val timeTakenString = metrics.timeTaken.toString() + " seconds"
        timeTaken.text = timeTakenString
        val percentageV = view.findViewById<TextView>(R.id.trail_metricsCompletePercentage)
        val perc = metrics.completedPercentage.toString()
        percentageV.text = perc
        val imagem = view.findViewById<ImageView>(R.id.trail_metricsImageDescription)
        Picasso.get().load(
            trail.imageUrl
                .replace("http", "https")
        )
            .into(imagem)
        val hardtext1 = view.findViewById<TextView>(R.id.trail_metricsDescriptionTitle)
        val hardtext2 = view.findViewById<TextView>(R.id.trail_percentage_hardtext)

        // Set text color based on theme mode
        val textColor: Int
        val cardColor: Int
        if (isDarkModeEnabled) {
            textColor = Color.WHITE
            cardColor = Color.GRAY
        } else {
            textColor = Color.BLACK
            cardColor = Color.WHITE
        }
        val cd = view.findViewById<CardView>(R.id.card_view)
        cd.setCardBackgroundColor(cardColor)
        timeTaken.setTextColor(textColor)
        percentageV.setTextColor(textColor)
        hardtext1.setTextColor(textColor)
        hardtext2.setTextColor(textColor)
    }

    private val isDarkModeEnabled: Boolean
        get() {
            val mainActivity = activity as MainActivity?
            return mainActivity?.isDarkModeEnabled ?: false
        }
}