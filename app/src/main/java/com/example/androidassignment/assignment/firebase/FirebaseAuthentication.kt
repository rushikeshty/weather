package com.example.androidassignment.assignment.firebase

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthentication() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun signUpToFirebase(username: String, password: String, listener: FirebaseListener) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.signInWithEmailAndPassword(username, password)
                    listener.onCompleteListener()
                } else {
                    listener.onFailureListener(it.exception.toString())
                }
            }
            .addOnFailureListener {
                listener.onFailureListener(it.message.toString())
            }
    }

    fun signInToFirebase(username: String, password: String, listener: FirebaseListener) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener.onCompleteListener()
                } else {
                    listener.onFailureListener(it.exception.toString())
                }
            }
            .addOnFailureListener {
                listener.onFailureListener(it.message.toString())
            }

    }

    fun getCurrentUser(listener: FirebaseListener) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            listener.onCompleteListener()
        } else {
            listener.onFailureListener("Login here")
        }
    }

    fun signOut() {
        auth.signOut()
    }


}