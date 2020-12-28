package com.example.wips.Activities.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import com.example.wips.R

class Search_Place : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var listview: ListView
        lateinit var listadapter: ArrayAdapter<String>
        var searchlist: MutableList<String> = ArrayList()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search__place)

        listview = findViewById(R.id.List_place)
        searchlist.add("Abhishek")
        searchlist.add("Gorivale")

        listadapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, searchlist
        )
        listview.adapter = listadapter

    }
}