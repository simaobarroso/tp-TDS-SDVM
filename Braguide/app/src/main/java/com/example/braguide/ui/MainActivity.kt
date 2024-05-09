package com.example.braguide.ui



import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.braguide.R
import com.example.braguide.databinding.ActivityMainBinding
import com.example.braguide.model.User
import com.example.braguide.viewModel.UserViewModel
import com.example.braguide.viewModel.UserViewModelFactory
import com.google.android.material.navigation.NavigationView
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var binding: ActivityMainBinding? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var navController: NavController
    private lateinit var sidebar: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val userModelFactory = UserViewModelFactory(application)
        userViewModel = ViewModelProvider(this, userModelFactory)[UserViewModel::class.java]

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        configureBottomNavigation(navController)
        checkDarkMode()
    }

    public override fun onStart() {
        super.onStart()
        checkLoggedIn()
    }

    private fun checkLoggedIn() {
        try {
            val userLiveData = userViewModel.user
            userLiveData.observe(
                this
            ) { user: User? ->
                if (user != null) {
                    if (user.userType == "loggedOff") {
                        Log.e("MAIN", "failed login")
                        changeToLoginActivity()
                    } else Log.e("MAIN", "login success")
                    userLiveData.removeObservers(this)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun changeToLoginActivity() {
        Log.e("DEBUG", "CHANGE TO LOGIN ACTIVITY")
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                navController.navigate(R.id.settingsFragment)
                true
            }
            R.id.profile -> {
                navController.navigate(R.id.profileFragment)
                true
            }
            R.id.logout -> {
                try {
                    userViewModel.logOut(
                        applicationContext,
                        object : UserViewModel.LogoutCallback {
                            override fun onLogoutSuccess() {
                                changeToLoginActivity()
                            }

                            override fun onLogoutFailure() {}
                        })
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
                true
            }

            else -> false
        }
    }

    private fun configureBottomNavigation(navController: NavController) {
        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.popular -> {
                    navController.navigate(R.id.popularFragment)
                    true
                }
                R.id.search -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }

                R.id.emergency -> {
                    navController.navigate(R.id.emergencyFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun checkDarkMode() {
        val wantsDarkMode: Boolean = userViewModel.getDarkModePreference(this)
        if (wantsDarkMode && isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (!wantsDarkMode && isDarkModeEnabled) AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    val isDarkModeEnabled: Boolean
        get() {
            val nightModeFlags =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        }
}
