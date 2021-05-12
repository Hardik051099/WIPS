package com.example.wips.Utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.wips.Activities.User.Navigation
import com.example.wips.R


class CustomDialogClass(context: Context, val name: String, val place: String) : Dialog(context){
    lateinit var dgbody:TextView
    lateinit var dgtitle:TextView
    lateinit var navbtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

        dgbody= findViewById(R.id.dgBody)
        dgtitle = findViewById(R.id.dgTitle)
        navbtn = findViewById(R.id.navbtn)

        dgtitle.text = "Name: "+ name
        dgbody.text = "Location: "+place
    }
}