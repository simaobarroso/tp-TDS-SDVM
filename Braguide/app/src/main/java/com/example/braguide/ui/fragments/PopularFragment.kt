package com.example.braguide.ui.fragments



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.R
import com.example.braguide.model.Trail
import com.example.braguide.model.User
import com.example.braguide.ui.TrailsRecyclerViewAdapter
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.viewModel.TrailsViewModelFactory
import com.example.braguide.viewModel.UserViewModel
import com.example.braguide.viewModel.UserViewModelFactory
import java.io.IOException

class PopularFragment : Fragment() {
    private var trailViewModel: TrailsViewModel? = null
    private var adapter: TrailsRecyclerViewAdapter? = null
    private var userViewModel: UserViewModel? = null

    override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ENTREI", "PopularFragment")

        val view: View = inflater.inflate(R.layout.fragment_popular, container, false)

        val viewModelFactory = TrailsViewModelFactory(requireActivity().application)
        trailViewModel = ViewModelProvider(this, viewModelFactory)[TrailsViewModel::class.java]

        val userModelFactory = UserViewModelFactory(requireActivity().application)
        userViewModel = ViewModelProvider(this, userModelFactory)[UserViewModel::class.java]
        try {
            trailViewModel!!.allTrails.observe(viewLifecycleOwner) { y ->
                try {
                    userViewModel!!.user.observe(viewLifecycleOwner) { user ->
                        if (user != null) {
                            loadRecyclerView(view, y, user)
                        }
                    }
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return view
    }

    private fun loadRecyclerView(view: View, trails: List<Trail>, user: User) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.trail_main_list)
        val gridLayoutManager = GridLayoutManager(recyclerView.context, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager

        adapter = TrailsRecyclerViewAdapter(trails)
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): PopularFragment {
            return PopularFragment()
        }
    }
}