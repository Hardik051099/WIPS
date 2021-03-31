package com.example.wips.Activities.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import com.example.wips.Adapters.FindUserAdapter
import com.example.wips.R

class Search_user : AppCompatActivity() {
    lateinit var listview: ListView
    lateinit var searchbar: SearchView
    var namelist = arrayOf<String>()
    var locationlist = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        namelist = arrayOf<String>("C","C++","Java",".Net","Kotlin","Ruby","Rails","Python","Java Script","Php","Ajax","Perl","Hadoop")
        locationlist = arrayOf<String>("C","C++","Java",".Net","Kotlin","Ruby","Rails","Python","Java Script","Php","Ajax","Perl","Hadoop")

        val myListAdapter = FindUserAdapter(this,namelist,locationlist)

        searchbar = findViewById(R.id.search_view_user)

        searchbar.setIconified(false)
        searchbar.clearFocus()

        listview = findViewById(R.id.List_users)
        listview.adapter = myListAdapter


    }
}