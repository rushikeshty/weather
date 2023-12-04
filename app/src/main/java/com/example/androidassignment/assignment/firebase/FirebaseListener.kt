package com.example.androidassignment.assignment.firebase

interface FirebaseListener {
    fun onCompleteListener()
    fun onFailureListener(error:String)
}