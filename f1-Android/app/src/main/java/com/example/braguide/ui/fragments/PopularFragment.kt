package com.example.braguide.ui.fragments




import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.braguide.R
import com.example.braguide.model.AppData
import com.example.braguide.model.Trail
import com.example.braguide.model.User
import com.example.braguide.ui.viewadapters.TrailsRecyclerViewAdapter
import com.example.braguide.viewModel.AppDataViewModel
import com.example.braguide.viewModel.AppDataViewModelFactory
import com.example.braguide.viewModel.TrailsViewModel
import com.example.braguide.viewModel.TrailsViewModelFactory
import com.example.braguide.viewModel.UserViewModel
import com.example.braguide.viewModel.UserViewModelFactory
import java.io.IOException


class PopularFragment : Fragment() {
    private var trailViewModel: TrailsViewModel? = null
    private var adapter: TrailsRecyclerViewAdapter? = null
    private var userViewModel: UserViewModel? = null
    private var appViewModel: AppDataViewModel? = null

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

        val appModelFactory = AppDataViewModelFactory(requireActivity().application)
        appViewModel = ViewModelProvider(this, appModelFactory)[AppDataViewModel::class.java]

        appViewModel!!.appData.observe(viewLifecycleOwner) {
            x -> loadView(view,x)
        }

        return view
    }

    private fun loadRecyclerView(view: View, trails: List<Trail>, user: User) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.trail_main_list)
        val gridLayoutManager = GridLayoutManager(recyclerView.context, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager

        adapter = TrailsRecyclerViewAdapter(trails)
        recyclerView.adapter = adapter

        if (user.userType == "Premium") {
            adapter?.setListener(object : TrailsRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(trail: Trail?) {
                    trail?.let { replaceFragment(it) }
                }
            })
        }else {
            adapter?.setListener(object : TrailsRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(trail: Trail?) {
                    Toast.makeText(
                        context,
                        "Only premium users can use this feature",
                        Toast.LENGTH_LONG
                    ).show()
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

    private fun loadView(view: View, appData: AppData) {
        val title = view.findViewById<TextView>(R.id.textView2)
        title.text = appData.appDesc
        val intro = view.findViewById<TextView>(R.id.appIntro)
        intro.text = appData.appLandingPageText
    }

    companion object {
        fun newInstance(): PopularFragment {
            return PopularFragment()
        }
    }
}