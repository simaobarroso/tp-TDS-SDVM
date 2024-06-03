package com.example.braguide.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.R
import com.example.braguide.model.Trail
import com.example.braguide.model.User
import com.example.braguide.ui.viewadapters.TrailsRecyclerViewAdapterSearch
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.viewModel.TrailsViewModelFactory
import com.example.braguide.viewModel.UserViewModel
import com.example.braguide.viewModel.UserViewModelFactory


class SearchFragment
    : Fragment() {
    private var mColumnCount = 1
    private lateinit var trailViewModel: TrailsViewModel
    private lateinit var searchView : SearchView
    private lateinit var adapter: TrailsRecyclerViewAdapterSearch


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
    ): View {
        Log.d("ENTREI", "SearchFragment")

        val view: ConstraintLayout = inflater.inflate(R.layout.fragment_search, container, false) as ConstraintLayout
        val tViewModelFactory = TrailsViewModelFactory(requireActivity().application)
        trailViewModel = ViewModelProvider(this, tViewModelFactory)[TrailsViewModel::class.java]
        val uViewModelFactory = UserViewModelFactory(requireActivity().application)
        val userViewModel = ViewModelProvider(this, uViewModelFactory)[UserViewModel::class.java]


        trailViewModel.allTrails.observe(
            viewLifecycleOwner
        ) { x: List<Trail>? ->
            x?.let {
                userViewModel.user.observe(viewLifecycleOwner) {
                    loadRecyclerView(view, x, it)
                }
            }
        }

        return view
    }

    private fun loadRecyclerView(rootView: ConstraintLayout, trails: List<Trail>, user: User) {
        val view = rootView.findViewById<View>(R.id.trail_recycle_view)
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(LinearLayoutManager(context))
            } else {
                recyclerView.setLayoutManager(GridLayoutManager(context, mColumnCount))
            }
            adapter = TrailsRecyclerViewAdapterSearch(trails)
            recyclerView.setAdapter(adapter)
            // Set the item click listener
            // Handle the item click event
            if (user.userType == "Premium") {
                adapter.setListener(object : TrailsRecyclerViewAdapterSearch.OnItemClickListener {
                    override fun onItemClick(trail: Trail?) {
                        trail?.let { replaceFragment(it) }
                    }
                })
            } else {
                adapter.setListener(object: TrailsRecyclerViewAdapterSearch.OnItemClickListener {
                    override fun onItemClick(trail: Trail?) {
                        Toast.makeText(
                            getContext(),
                            "Only premium users can use this feature",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }

            adapter.reset()
            searchView = rootView.findViewById(R.id.search_view)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.d("SEARCH", query)
                    if (query == "") {
                        adapter.reset()
                    } else {
                        adapter.filterData(query)
                    }
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText == "") {
                        adapter.reset()
                    } else {
                        adapter.filterData(newText)
                    }
                    return true
                }
            })
        }
    }

    private fun replaceFragment(trail: Trail) {
        val bundle = Bundle()
        bundle.putInt("id", trail.id)
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navHostFragment!!.navController.navigate(R.id.trailDescriptionFragment, bundle)
    }


    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCount: Int): SearchFragment {
            val fragment = SearchFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.setArguments(args)
            return fragment
        }
    }
}