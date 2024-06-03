package com.example.braguide.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.Trail
import com.example.braguide.model.TrailMetrics
import com.example.braguide.viewModel.UserViewModel
import java.util.stream.Collectors
import com.example.braguide.R
import com.example.braguide.ui.viewadapters.TrailMetricsRecyclerViewAdapter
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.viewModel.TrailsViewModelFactory
import com.example.braguide.viewModel.UserViewModelFactory


class HistoryFragment : Fragment() {
    private var mColumnCount = 1
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
        val view: View =
            inflater.inflate(R.layout.fragment_trail_item_metrics_list, container, false)

        val userModelFactory = UserViewModelFactory(requireActivity().application)
        val userViewModel = ViewModelProvider(this, userModelFactory)[UserViewModel::class.java]

        val viewModelFactory = TrailsViewModelFactory(requireActivity().application)
        val trailViewModel = ViewModelProvider(this, viewModelFactory)[TrailsViewModel::class.java]

        userViewModel.getMetrics() .observe(getViewLifecycleOwner()) { metrics ->
            if (metrics != null && metrics.isNotEmpty()) {
                trailViewModel.getTrailsById(
                    metrics.stream()
                        .map(TrailMetrics::trailId)
                        .collect(Collectors.toList())
                )
                    .observe(getViewLifecycleOwner()) { trails ->
                        if (trails != null && metrics.size == trails.size) {
                            loadRecyclerView(view, metrics, trails)
                        }
                    }
            }
        }
        return view
    }

    private fun loadRecyclerView(view: View, metrics: List<TrailMetrics>, trails: List<Trail>) {
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(LinearLayoutManager(context))
            } else {
                recyclerView.setLayoutManager(GridLayoutManager(context, mColumnCount))
            }
            val adapter = TrailMetricsRecyclerViewAdapter(metrics, trails)
            recyclerView.setAdapter(adapter)
            // Set the item click listener
            // Handle the item click event
            adapter.setClickListener(object : TrailMetricsRecyclerViewAdapter.ClickListener {
                override fun onItemClick(metrics: TrailMetrics?) {
                    metrics?.let { replaceFragment(it) }
                }
            })
        }
    }

    private fun replaceFragment(trailMetrics: TrailMetrics) {
        val bundle = Bundle()
        bundle.putInt("id", trailMetrics.metricId!!)
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navHostFragment!!.navController.navigate(R.id.trailMetricsDescriptionFragment, bundle)
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCount: Int): HistoryFragment {
            val fragment = HistoryFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.setArguments(args)
            return fragment
        }
    }
}