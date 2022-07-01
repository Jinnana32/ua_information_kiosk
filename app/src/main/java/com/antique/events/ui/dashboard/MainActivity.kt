package com.antique.events.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.antique.events.LoginActivity
import com.antique.events.R
import com.antique.events.data.services.LocalStorageService
import com.antique.events.ui.dashboard.appointment.AppointmentFragment
import com.antique.events.ui.dashboard.home.HomeFragment
import com.antique.events.ui.dashboard.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) : Intent {
            return Intent(context, MainActivity::class.java);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = toolbar as Toolbar
        setSupportActionBar(toolbar)
        if(supportActionBar !== null){
            supportActionBar!!.setDisplayShowTitleEnabled(false);
        }

        toolbar_username.text = "Current user" //LocalStorageService.getUserName();

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(navListener)

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, HomeFragment()).commit()

       /* val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_appointment, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/

        toolbar_logout.setOnClickListener {
            LocalStorageService.removeUser()
            startActivity(LoginActivity.getIntent(this@MainActivity));
            finish();
        }
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item -> // By using switch we can easily get
            // the selected fragment
            // by using there id.
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_home -> selectedFragment = HomeFragment()
                R.id.navigation_profile -> selectedFragment = ProfileFragment()
                R.id.navigation_downloads -> selectedFragment = AppointmentFragment()
            }
            // It will help to replace the
            // one fragment to other.
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, selectedFragment!!)
                .commit()
            true
        }

}