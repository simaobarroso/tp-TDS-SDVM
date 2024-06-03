package com.example.braguide.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.Edge
import com.example.braguide.model.Trail
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.R
import com.example.braguide.ui.viewadapters.EdgesRecyclerViewAdapter
import com.example.braguide.viewModel.TrailsViewModelFactory
import java.util.Objects

import java.util.stream.Collectors


class EdgeListFragment : Fragment() {
    private var trailViewModel: TrailsViewModel? = null
    private var type: String? = null
    private lateinit var ids: List<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val idsList: java.util.ArrayList<Int>? = requireArguments().getIntegerArrayList(ARG_TRAIL_LIST)
            if (idsList != null) {
                ids = idsList
                type = "trails"
            } else {
                ids = requireArguments().getIntegerArrayList(ARG_PIN_LIST)!!
                type = "pins"
                requireNotNull(ids) { "Both trail ID list and pin ID list are null" }
            }
        } else {
            throw IllegalArgumentException("Arguments bundle is null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_edge_list, container, false)
        val viewModelFactory = TrailsViewModelFactory(requireActivity().application)
        trailViewModel = ViewModelProvider(this, viewModelFactory)[TrailsViewModel::class.java]
        if (Objects.equals(type, "trails")) {
            trailViewModel!!.getTrailsById(ids).observe(getViewLifecycleOwner()) { trails ->
                val edges: List<Edge> =
                    trails.stream()
                        .map(Trail::edges)
                        .flatMap { obj: List<Edge> -> obj.stream() }
                        .distinct()
                        .collect(Collectors.toList())
                loadView(view, edges)
            }
        }
        return view
    }

    private fun loadView(view: View, edges: List<Edge>) {
        if (view is RecyclerView) {
            val recyclerView = view
            val gridLayoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            recyclerView.setLayoutManager(gridLayoutManager)
            val adapter = EdgesRecyclerViewAdapter(edges, requireActivity())
            recyclerView.setAdapter(adapter)
        }
    }

    companion object {
        private const val ARG_TRAIL_LIST = "TRAIL_LIST"
        private const val ARG_PIN_LIST = "PIN_LIST"
        fun newInstanceByTrails(ids: List<Int>?): EdgeListFragment {
            val fragment = EdgeListFragment()
            val args = Bundle()
            args.putIntegerArrayList(ARG_TRAIL_LIST, ArrayList(ids))
            fragment.setArguments(args)
            return fragment
        }

        fun newInstanceByPins(ids: List<Int>?): EdgeListFragment {
            val fragment = EdgeListFragment()
            val args = Bundle()
            args.putIntegerArrayList(ARG_PIN_LIST, ArrayList(ids))
            fragment.setArguments(args)
            return fragment
        }
    }
}