package com.example.braguide.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.model.Trail
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.R


/**
 * A fragment representing a list of Items.
 */
class TrailListFragment  // private List<Trail> trails = new ArrayList<>();
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
    : Fragment() {
    private var mColumnCount = 1
    private var trailsViewModel: TrailsViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mColumnCount = requireArguments().getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_item_list, container, false)
        val localTrailsViewModel = trailsViewModel // Store reference in a local variable

        localTrailsViewModel?.allTrails?.observe(
            viewLifecycleOwner
        ) { x: List<Trail>? ->
            x?.let {
                loadRecyclerView(view, it)
            }
        }

        return view
    }

    private fun loadRecyclerView(view: View, trails: List<Trail>) {
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(LinearLayoutManager(context))
            } else {
                recyclerView.setLayoutManager(GridLayoutManager(context, mColumnCount))
            }
            recyclerView.setAdapter(TrailsRecyclerViewAdapter(trails))
        }
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCount: Int): TrailListFragment {
            val fragment = TrailListFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.setArguments(args)
            return fragment
        }
    }
}