package com.example.androidassignment.assignment

import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class SessionManager(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("USER_SESSION", MODE_PRIVATE)

     fun createLoginRegisterSession(name:String,userName: String, passWord: String) {
        sharedPreferences.edit().apply {
            putString("name",name)
            putString("userName", userName)
            putString("passWord", passWord)
            putBoolean("status", true)
           apply()
        }

    }

    /*To check whether user is already logged in or not*/
    fun validateSession(): Boolean {
        val user   = sharedPreferences.getString("userName", "")
        val pass   = sharedPreferences.getString("passWord", "")
        val status = sharedPreferences.getBoolean("status", true)

        if ((user != null) && (pass != null) && user.isNotBlank() && pass.isNotBlank() && status) {

            return true
        }
        return false
    }

    fun validateLoginSession(userName:String,passWord: String): Boolean {
        val name = sharedPreferences.getString("name","")
        val user   = sharedPreferences.getString("userName", "")
        val pass   = sharedPreferences.getString("passWord", "")

        if ((user != null) && (pass != null) && userName.equals(user)&&passWord.equals(pass)) {
            sharedPreferences.edit().apply {
                putString("name",name)
                putString("userName", userName)
                putString("passWord", passWord)
                putBoolean("status", true)
                apply()
            }
            return true
        }
        return false
    }

    /*  fun getDetail(key:String):String?{
          return sharedPreferences.getString(key,null)
      }

      fun reCreateSession(status:Boolean){
         sharedPreferences.edit{
             putBoolean("status",true)
         }
      }*/

    fun callLogOutSession(status: Boolean) {
        val name = sharedPreferences.getString("name","")
        val user   = sharedPreferences.getString("userName", "")
        val pass   = sharedPreferences.getString("passWord", "")

        sharedPreferences.edit {
            putString("name",name)
            putString("userName", user)
            putString("passWord", pass)
            putBoolean("status", status)
        }
    }

    fun getName(): String {
        return sharedPreferences.getString("name", "")!!
    }
    fun putFirstTime() {
        sharedPreferences.edit().apply {
            putString("firstTime", "yes")
        }.apply()
    }

    fun getFirstTime(): String {
        val firstTime = sharedPreferences.getString("firstTime", "")
        return firstTime.toString()
    }


}