package com.example.wips.Activities.Ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wips.Activities.Login
import com.example.wips.R
import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.example.wips.Activities.User.User_home

class MainActivity : AppCompatActivity() {

    lateinit var anim_fade_in : Animation
    private val RECORD_REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var wifi_img = findViewById<ImageView>(R.id.wifi_logo)

        val isLoggedin = getSharedPreferences("Loggedin", Context.MODE_PRIVATE).getBoolean("isLoggedin", false)



        //Create Animation
        anim_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in)

        anim_fade_in.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }
            override fun onAnimationEnd(animation: Animation) {

                if(isLoggedin){
                    startActivity(Intent(this@MainActivity,User_home::class.java))
                }
                else{
                    startActivity(Intent(this@MainActivity, Login::class.java))
                    finish()
                }
            }
            override fun onAnimationRepeat(animation: Animation) {
            }
        })

        //Trigger Animation
        wifi_img.startAnimation(anim_fade_in)

    }


}