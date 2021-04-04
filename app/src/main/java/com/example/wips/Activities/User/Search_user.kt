package com.example.wips.Activities.User

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wips.Adapters.FindUserAdapter
import com.example.wips.R
import com.example.wips.Utils.CustomDialogClass
import java.util.*
import kotlin.collections.ArrayList


class Search_user : AppCompatActivity() {
    lateinit var listview: ListView
    lateinit var searchbar: SearchView

    var namelist = arrayOf<String>()
    var locationlist = arrayOf<String>()


    var tempnamelist: MutableList<String> = ArrayList()
    var templocationlist: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        namelist = arrayOf<String>("Abhishek", "Hardik", "Aniket", "Kiran", "Harshal", "Archis", "Ritesh")
        locationlist = arrayOf<String>("Library 2nd floor", "CN lab 3rd floor", "DS lab 1st floor", "Library 4th floor", "OS LAB 3rd floor", "Library 2nd floor", "Office 1st floor")

        var myListAdapter = FindUserAdapter(this, namelist, locationlist)
        listview = findViewById(R.id.List_users)
        listview.adapter = myListAdapter

        searchbar = findViewById(R.id.search_view_user)
        searchbar.setIconified(false)
        searchbar.clearFocus()

        Handler().postDelayed(
                { Log.i("Tag", "Hii") }, 1000
        )

        searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("Submit", query)
                if (query != null) {

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Toast.makeText(view.context,newText,Toast.LENGTH_SHORT).show()
                if (newText != null) {
                    tempnamelist.clear()
                    templocationlist.clear()
                    namelist.forEach {
                        if (it.toUpperCase().contains(newText.toUpperCase())) {
                            tempnamelist.add(it)
                            templocationlist.add(locationlist.get(it.indexOf(it)))
                        }
                    }
                    Log.d("tempname", tempnamelist.toString())
                    Log.d("temploc", templocationlist.toString())
                    myListAdapter = FindUserAdapter(this@Search_user, tempnamelist.toTypedArray(), templocationlist.toTypedArray())
                    listview.adapter = myListAdapter
                }
                return true
            }

        })



        listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val name = parent.getItemAtPosition(position).toString()
            val place = locationlist.get(namelist.indexOf(name))
            var dialog = CustomDialogClass(this, name, place)
            dialog.show()
            dialog.navbtn.setOnClickListener {
                startActivity(Intent(this,Navigation::class.java).putExtra("Navplace",place))
            }
        }

    }
}