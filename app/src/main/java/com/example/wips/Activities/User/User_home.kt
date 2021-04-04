package com.example.wips.Activities.User

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.wips.Activities.Admin.Buildings
import com.example.wips.Activities.Login
import com.example.wips.Models.Map
import com.example.wips.R
import com.example.wips.Utils.Database
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import showCustomToast

class User_home : AppCompatActivity() {

    lateinit var card1_img: ImageView
    lateinit var  card1_title: TextView
    lateinit var  card1_desc: TextView

    lateinit var card2_img: ImageView
    lateinit var  card2_title: TextView
    lateinit var  card2_desc: TextView

    lateinit var database: Database

    lateinit var navimg: ImageView
    lateinit var navigation_view:NavigationView


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        //Following code is for navigation drawer ->

        navimg = findViewById<ImageView>(R.id.navimg)
        navigation_view = findViewById(R.id.navigation_view)
        val navDrawer: DrawerLayout = findViewById(R.id.drawerlayout)
        navimg.setOnClickListener {
            // If the navigation drawer is not open then open it, if its already open then close it.
            // If the navigation drawer is not open then open it, if its already open then close it.
            if (!navDrawer.isDrawerOpen(Gravity.START)) navDrawer.openDrawer(Gravity.START)
            else navDrawer.closeDrawer(Gravity.END)
        }

        navigation_view.setNavigationItemSelectedListener{
            when (it.itemId){
                R.id.action_changeplace->{
                    startActivity(Intent(this, Search_Place::class.java))
                }

                R.id.action_privacy ->{}
                R.id.action_report ->{
                    startActivity(Intent(this, report_issue::class.java))
                }

                R.id.action_logout ->{
                    getSharedPreferences("Loggedin", Context.MODE_PRIVATE).edit()
                            .putBoolean("isLoggedin", false).apply()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this,Login::class.java))
                }

            }
            // Close the drawer
            navDrawer.closeDrawer(GravityCompat.START)
            true
        }

        //Following code is for cards in the activity ->

        val myCard1: View = findViewById(R.id.card1)
        card1_img = myCard1.findViewById(R.id.card_img) as ImageView
        card1_title = myCard1.findViewById(R.id.card_head) as TextView
        card1_desc = myCard1.findViewById(R.id.card_descriptor) as TextView
        card1_img.setImageResource(R.drawable.cardnav)
        card1_title.setText("Navigation")
        card1_desc.setText("Explore Nearby")

        val myCard2: View = findViewById(R.id.card2)
        card2_img = myCard2.findViewById(R.id.card_img) as ImageView
        card2_title = myCard2.findViewById(R.id.card_head) as TextView
        card2_desc = myCard2.findViewById(R.id.card_descriptor) as TextView
        card2_img.setImageResource(R.drawable.cardfind)
        card2_title.setText("Search")
        card2_desc.setText("Find someone")


        myCard1.setOnClickListener{

            Toast.makeText(this, "Entered navigation", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Navigation::class.java))
        }

        myCard2.setOnClickListener{
            startActivity(Intent(this,Search_user::class.java))
        }

    }
}