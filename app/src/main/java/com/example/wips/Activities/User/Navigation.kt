package com.example.wips.Activities.User

import android.content.Intent
import android.os.Bundle
import android.os.WorkSource
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wips.R

class Navigation : AppCompatActivity() {
    lateinit var currentplace:String
    lateinit var navigateplace:String

    lateinit var source: TextView
    lateinit var desti:TextView
    lateinit var navbtn:Button

    lateinit var cmd_img:ImageView
    lateinit var cmd_txt:TextView
    lateinit var txtv_place:TextView
    lateinit var txtv_floor:TextView
    lateinit var txtv_building:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        source=findViewById(R.id.source)
        desti=findViewById(R.id.destination)
        navbtn=findViewById(R.id.navbtnnavigation)

        cmd_img=findViewById(R.id.cmd_img)
        cmd_txt=findViewById(R.id.cmd_txt)
        txtv_place=findViewById(R.id.txt_place)
        txtv_floor=findViewById(R.id.txt_floor)
        txtv_building=findViewById(R.id.txt_building)

        currentplace= current_path
        source.setText(currentplace.substringAfter("/").replace("/",", "))
        source.setEnabled(false)

        if (!navigation_path.isNullOrEmpty()){
            navigateplace= navigation_path
            desti.setText(navigateplace.substringAfter("/").replace("/",", "))
        }
        desti.setOnClickListener {
            startActivity(Intent(this,Find_Destination::class.java))
            finish()
        }

        navbtn.setOnClickListener {
            navigate()
        }

    }
    fun navigate(){
        var current_campus=currentplace.substringBefore("/")
        currentplace=currentplace.substringAfter("/")
        var current_Building=currentplace.substringBefore("/")
        currentplace=currentplace.substringAfter("/")
        var current_floor = currentplace.substringBefore("/")
        currentplace=currentplace.substringAfter("/")
        var current_place = currentplace.substringBefore("/")

        var navigate_campus=navigateplace.substringBefore("/")
        navigateplace=navigateplace.substringAfter("/")
        var navigate_Building=navigateplace.substringBefore("/")
        navigateplace=navigateplace.substringAfter("/")
        var navigate_floor = navigateplace.substringBefore("/")
        navigateplace=navigateplace.substringAfter("/")
        var navigate_place = navigateplace.substringBefore("/")

        txtv_place.setText(txtv_place.text.toString() + " " + current_place)
        txtv_floor.setText(txtv_floor.text.toString() + " " + current_floor)
        txtv_building.setText(txtv_building.text.toString() + " " + current_Building)

        if (current_campus.equals(navigate_campus)){
            if (current_Building.equals(navigate_Building)){
                if (current_floor.equals(navigate_floor)){
                    if (current_place.equals(navigate_place)){
                        //Reached
                        cmd_txt.setText("Reached destination")
                        cmd_img.setImageResource(R.drawable.arrived)
                        Log.d("Command","Reached")
                    }
                    else{
                        //Nearby
                        cmd_txt.setText("You are nearby")
                        cmd_img.setImageResource(R.drawable.near_by)
                        Log.d("Command","Nearby")
                    }
                }
                else if (current_floor.toInt() < navigate_floor.toInt()){
                    //go upstairs
                    cmd_txt.setText("Go upstairs")
                    cmd_img.setImageResource(R.drawable.upstairs)
                    Log.d("Command","Go upstairs")
                }
                else{
                    //go downstairs
                    cmd_txt.setText("Go downstairs")
                    cmd_img.setImageResource(R.drawable.downstairs)
                    Log.d("Command","Go downstairs")
                }
            }
            else{
                //Change Building
                cmd_txt.setText("Change Building")
                cmd_img.setImageResource(R.drawable.buildings)
                Log.d("Command","Change Building")
            }
        }
        else{
            //Change campus
            cmd_txt.setText("Change campus")
            cmd_img.setImageResource(R.drawable.campus)
            Log.d("Command","Change campus")
        }
    }
}