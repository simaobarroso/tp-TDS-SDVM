package com.example.braguide.ui


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.braguide.R
import com.example.braguide.databinding.ActivityMainBinding
import com.example.braguide.model.User
import com.example.braguide.viewModel.UserViewModel
import com.example.braguide.viewModel.UserViewModelFactory
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private var toolbar: Toolbar? = null
    private var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var binding: ActivityMainBinding? = null
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val userModelFactory = UserViewModelFactory(application)
        userViewModel = ViewModelProvider(this, userModelFactory)[UserViewModel::class.java]


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        drawerLayout = findViewById(R.id.drawerLayout)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout?.addDrawerListener(drawerToggle!!)
        drawerToggle?.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        configureSideBar(navController)
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

    private fun configureSideBar(navController: NavController) {
        binding?.sidebar?.setNavigationItemSelectedListener { menuItem : MenuItem ->
            val itemId: Int = menuItem.itemId
            when(itemId) {
                R.id.popular -> navController.navigate(R.id.popularFragment)
                R.id.search -> navController.navigate(R.id.searchFragment)
                R.id.profile -> navController.navigate(R.id.profileFragment)
                R.id.emergency -> navController.navigate(R.id.emergencyFragment)
                R.id.history -> navController.navigate(R.id.historyFragment)
                R.id.settings -> navController.navigate(R.id.settingsFragment)
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
                    }
                    catch (e : IOException) {
                        throw RuntimeException(e)
                    }
                }


            }

            val menu: Menu = binding!!.sidebar.menu
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                if (item.isChecked) {
                    item.setChecked(false)
                }
            }


            // Close side bar
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        //Allows side-bar items to be selected
        binding!!.sidebar.bringToFront()

        userViewModel.user.observe(this) { user ->
            val nameTextView = findViewById<TextView>(R.id.header_title)
            nameTextView?.text = user.username
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return if (drawerToggle!!.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(
            item
        )
        // Otherwise, let the default behavior handle the click event
    }


    private val isDarkModeEnabled: Boolean
        get() {
            val nightModeFlags =
                getResources().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
        }
}