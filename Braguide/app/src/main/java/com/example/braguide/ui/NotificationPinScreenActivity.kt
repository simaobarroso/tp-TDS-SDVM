package com.example.braguide.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import android.os.Bundle
import com.example.braguide.model.EdgeTip
import com.example.braguide.R
import com.example.braguide.ui.fragments.PinFragment
import java.io.Serializable


class NotificationPinScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificationpin)
        if (intent != null) {
            if (intent.hasExtra("EdgeTip")) {
                val edgeTip = intent.serializable<EdgeTip>("EdgeTip")
                val bundle = Bundle()
                bundle.putInt("id", edgeTip!!.id)
                val fragment: PinFragment = PinFragment.newInstance(edgeTip.id)
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.conteudo_pin, fragment)
                fragmentTransaction.commit()
            }
        }
    }
    inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    companion object {
        fun newInstance(): NotificationPinScreenActivity {
            return NotificationPinScreenActivity()
        }
    }
}