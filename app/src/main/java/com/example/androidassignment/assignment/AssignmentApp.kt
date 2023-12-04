package com.example.androidassignment.assignment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.androidassignment.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class AssignmentApp : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_app)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.displayName.toString().contains("login")) {
                    finish()
                }
                else if (navController.currentDestination?.displayName.toString()
                        .contains("register")
                )
                {
                  navController.navigate(R.id.action_registerFragment_to_loginFragment)
                }
                else {
                    finish()
                }

            }
        })
    }
}