package com.example.braguide.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.braguide.viewModel.UserViewModel
import java.io.IOException
import com.example.braguide.R


class LoginActivity : AppCompatActivity() {
    private var name: EditText? = null
    private var password: EditText? = null
    private var login: Button? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //do nothing
            }
        }
        setContentView(R.layout.activity_login)
        val rootView = findViewById<View>(android.R.id.content)
        name = findViewById(R.id.name_input)
        password = findViewById(R.id.password_input)
        login = findViewById(R.id.btnLogin)
        rootView.setOnTouchListener { _, _ ->
            // Hide the keyboard and clear focus from the input form
            val imm =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(rootView.windowToken, 0)
            name!!.clearFocus()
            password!!.clearFocus()
            false // Return false to allow touch events to be passed to other views
        }
        login!!.setOnClickListener(View.OnClickListener {
            try {
                validate(name?.getText().toString(), password?.getText().toString())
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        })
    }

    private fun changeToMainActivity() {
        val intent = Intent(
            this@LoginActivity,
            MainActivity::class.java
        )
        Log.e("DEBUG", "INTENT START")
        startActivity(intent)
    }

    @Throws(IOException::class)
    private fun validate(userName: String, userPassword: String) {
        applicationContext //Context is required to right cookies in SharedPreferences
        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.login(
            userName.trim { it <= ' ' },
            userPassword,
            applicationContext,
            object : UserViewModel.LoginCallback {
                override fun onLoginSuccess() {
                    changeToMainActivity()
                }

                override fun onLoginFailure() {
                    val txtView = findViewById<TextView>(R.id.login_failed_txt)
                    txtView.visibility = View.VISIBLE
                }
            })
    }

    companion object {
        fun newInstance(): LoginActivity {
            return LoginActivity()
        }
    }
}