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
var current_path:String = ""   //Globally Declared ,can be used in any activity
var navigation_path:String = ""   //Globally Declared ,can be used in any activity

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

    lateinit var current_campus: String                 //This variable is having campus name use this to fetch values from db
    lateinit var current_location: TextView

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        current_location = findViewById(R.id.Locationtext_userhome)
        current_campus = getSharedPreferences("Campus_db", Context.MODE_PRIVATE).getString("Campus_db_value", "Unknown")
        current_location.text = "Location: " + current_campus

        current_path = "campus1/Rajkishan Building/3/room no 201"//This path will get stored in variable as well as on db



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
            if(!current_path.isNullOrEmpty()){
                startActivity(Intent(this, Navigation::class.java))
            }
        }

        myCard2.setOnClickListener{
            if(!current_path.isNullOrEmpty()){
                startActivity(Intent(this,Search_user::class.java))
            }
        }



    }

    //Use this function to find current position
    fun findposition(){
        //current_campus : Search for values in this campus
        // make a variable name current_path having String in format "Campus name/Building name/Floor no./Room name"
        //Eg.
        current_path = "campus1/Rajkishan Building/3/room no 201"  //make it default "Null"
    }
}