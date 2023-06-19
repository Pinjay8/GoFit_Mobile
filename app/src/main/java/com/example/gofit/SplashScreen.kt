package com.example.gofit

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper


class SplashScreen : AppCompatActivity() {
//    private val myPreference = "myPref"
//    private val name = "nameKey"
//    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        },3000)


//        sharedPreferences = getSharedPreferences(myPreference,
//            MODE_PRIVATE)
//        if (sharedPreferences!!.contains(name)) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }else {
//            setContentView(R.layout.activity_splash_screen)
//            Handler().postDelayed({
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }, 2000)
//
//            val editor: SharedPreferences.Editor =
//                sharedPreferences!!.edit()
//            editor.putString(name, "oke")
//            editor.apply()
//        }


    }
}