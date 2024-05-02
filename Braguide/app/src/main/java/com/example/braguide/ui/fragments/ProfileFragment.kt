package com.example.braguide.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.braguide.model.User
import com.example.braguide.viewModel.UserViewModel
import java.io.IOException
import com.example.braguide.R
import com.example.braguide.viewModel.UserViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val userModelFactory = UserViewModelFactory(requireActivity().application)
        userViewModel = ViewModelProvider(this, userModelFactory)[UserViewModel::class.java]
        try {
            userViewModel.user.observe(
                viewLifecycleOwner
            ) { x: User ->
                loadView(view, x)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return view
    }

    private fun loadView(view: View, user: User) {
        val username : TextView = view.findViewById(R.id.profile_username)
        username.text = user.username
        val userType : TextView = view.findViewById(R.id.profile_usertype)
        userType.text = user.userType
        val fullName : TextView = view.findViewById(R.id.profile_fullname)
        fullName.text = user.firstName + user.lastName
        val email : TextView = view.findViewById(R.id.profile_email)
        var emailString = user.email
        if (emailString == "") emailString = "No Email"
        email.text = emailString

        val button : Button = view.findViewById(R.id.profile_trailsHist_button)
        button.setOnClickListener { replaceFragment() }
    }

    private fun replaceFragment() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.navigate(R.id.trailHistoryFragment)
    }


    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}