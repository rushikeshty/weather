package com.example.androidassignment.assignment.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidassignment.R
import com.example.androidassignment.assignment.SessionManager
import java.util.Locale

class SplashFragment : Fragment(R.layout.fragment_start) {

    private var notfirsttime: Int = 0
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
        sessionManager = SessionManager(requireContext())

        if (sessionManager.getFirstTime() == "yes") {
            notfirsttime = 1
            if(findNavController().currentDestination!=null){
                findNavController().navigate(R.id.action_startFragment_to_loginFragment)

            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startButton = view.findViewById<Button>(R.id.start_Button)
        val textView2 = view.findViewById<TextView>(R.id.textView2)

        if (notfirsttime == 0) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.alpha)
            textView2.animation = animation
            startButton.visibility = View.INVISIBLE

            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed({
                startButton.visibility = View.VISIBLE
                startButton.animation = animation

            }, 5000)
        }

        startButton.setOnClickListener {
            sessionManager.putFirstTime()
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }
    }
}