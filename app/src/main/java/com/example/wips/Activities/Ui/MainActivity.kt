package com.example.wips.Activities.Ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.wips.Activities.Login
import com.example.wips.R

class MainActivity : AppCompatActivity() {

    lateinit var anim_fade_in : Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var wifi_img = findViewById<ImageView>(R.id.wifi_logo)

        //Create Animation
        anim_fade_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in)

        anim_fade_in.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }
            override fun onAnimationEnd(animation: Animation) {
                startActivity(Intent(this@MainActivity, Login::class.java))
                finish()
            }
            override fun onAnimationRepeat(animation: Animation) {
            }
        })

        //Trigger Animation
        wifi_img.startAnimation(anim_fade_in)

    }
}