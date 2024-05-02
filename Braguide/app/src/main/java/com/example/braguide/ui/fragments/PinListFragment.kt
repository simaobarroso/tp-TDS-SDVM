package com.example.braguide.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.EdgeTip
import com.example.braguide.viewModel.TrailsViewModel
import java.util.stream.Collectors
import java.util.stream.Stream
import com.example.braguide.R
import com.example.braguide.ui.viewadapters.PinsRecyclerViewAdapter
import com.example.braguide.viewModel.TrailsViewModelFactory
import java.util.Objects


class PinListFragment : Fragment() {
    private var trailViewModel: TrailsViewModel? = null
    private var type: String? = null
    private lateinit var ids: List<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val idsList: java.util.ArrayList<Int>? = arguments?.getIntegerArrayList(ARG_TRAIL_LIST)
            if (idsList != null) {
                ids = idsList
                type = "trails"
            } else {
                ids = requireArguments().getIntegerArrayList(ARG_PIN_LIST)!!
                type = "pins"
                requireNotNull(ids) { "Both trail ID list and pin ID list are null" }
            }
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
                val edgeTips: List<EdgeTip> = trails.stream()
                    .map { e ->
                        e.edges
                            .stream()
                            .flatMap { es ->
                                Stream.of(
                                    es.edgeStart,
                                    es.edgeEnd
                                )
                            }
                            .collect(Collectors.toList())
                    }
                    .flatMap { obj: List<EdgeTip> -> obj.stream() }
                    .distinct()
                    .collect(Collectors.toList())
                loadView(view, edgeTips)
            }
        } else if (type === "pins") {
            trailViewModel!!.getPinsById(ids).observe(getViewLifecycleOwner()) { x ->
                loadView(
                    view,
                    x
                )
            }
        }
        return view
    }

    private fun loadView(view: View, edgeTips: List<EdgeTip>) {
        if (view is RecyclerView) {
            val recyclerView = view
            val gridLayoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            recyclerView.setLayoutManager(gridLayoutManager)
            val adapter = PinsRecyclerViewAdapter(edgeTips)
            recyclerView.setAdapter(adapter)
            adapter.setOnItemClickListener(object : PinsRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(ed: EdgeTip?) {
                    ed?.let { replaceFragment(it) }
                }
            })
        }
    }

    private fun replaceFragment(edgeTip: EdgeTip) {
        val bundle = Bundle()
        bundle.putInt("id", edgeTip.id)
        val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.pinFragment, bundle)
    }

    companion object {
        private const val ARG_TRAIL_LIST = "TRAIL_LIST"
        private const val ARG_PIN_LIST = "PIN_LIST"
        fun newInstanceByTrails(ids: List<Int>?): PinListFragment {
            val fragment = PinListFragment()
            val args = Bundle()
            args.putIntegerArrayList(ARG_TRAIL_LIST, ArrayList(ids))
            fragment.setArguments(args)
            return fragment
        }

        fun newInstanceByPins(ids: List<Int>?): PinListFragment {
            val fragment = PinListFragment()
            val args = Bundle()
            args.putIntegerArrayList(ARG_PIN_LIST, ArrayList(ids))
            fragment.setArguments(args)
            return fragment
        }
    }
}