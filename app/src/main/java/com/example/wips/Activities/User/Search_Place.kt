package com.example.wips.Activities.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import com.example.wips.R

class Search_Place : AppCompatActivity() {

    lateinit var listview: ListView
    lateinit var listadapter: ArrayAdapter<String>
    lateinit var searchbar: SearchView
    var searchlist: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search__place)


        listview = findViewById(R.id.List_place)
        searchbar = findViewById(R.id.search_view)

        searchbar.setIconified(false)
        searchbar.clearFocus()


        for (i in 0..10){

            searchlist.add("Abhishek")
            searchlist.add("Gorivale")

        }
        listadapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, searchlist
        )
        listview.adapter = listadapter



    }

    fun View.hideKeyboard() {
        val inputMethodManager =
                context!!.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
    }
}