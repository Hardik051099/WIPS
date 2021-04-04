package com.example.wips.Activities.User

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.example.wips.Adapters.FindUserAdapter
import com.example.wips.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class Search_Place : AppCompatActivity() {

    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth

    lateinit var listview: ListView
    lateinit var listadapter: ArrayAdapter<String>
    lateinit var searchbar: SearchView
    var searchlist: MutableList<String> = ArrayList()
    var tempsearchlist:  MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search__place)


        listview = findViewById(R.id.List_place)
        searchbar = findViewById(R.id.search_view)

        searchbar.setIconified(false)
        searchbar.clearFocus()

        searchlist.add("No data found !")
        listadapter = ArrayAdapter(
                this@Search_Place,
                android.R.layout.simple_list_item_1, searchlist
        )
        listview.adapter = listadapter

        //Firebase Database Instance and Referance
        db= FirebaseDatabase.getInstance()
        dbrefer=db.getReference()

        dbrefer.child("Campus").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d("Data",p0.key.toString())
                searchlist.remove("No data found !")
                searchlist.add(p0.key.toString())
                listadapter = ArrayAdapter(
                        this@Search_Place,
                        android.R.layout.simple_list_item_1, searchlist
                )
                listview.adapter = listadapter

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })

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
                    tempsearchlist.clear()
                    searchlist.forEach {
                        if(it.toUpperCase().contains(newText.toUpperCase())){
                            tempsearchlist.add(it)
                        }
                    }
                    listadapter = ArrayAdapter(
                            this@Search_Place,
                            android.R.layout.simple_list_item_1, tempsearchlist
                    )
                    listview.adapter = listadapter
                }
                return true
            }
        })

        listview.setOnItemClickListener { parent, view, position, id ->
            if (!(parent.getItemAtPosition(position).toString().contains("No data found !"))) {
                val Campus_db = getSharedPreferences("Campus_db", Context.MODE_PRIVATE).getString("Campus_db_value", "Unknown")
                getSharedPreferences("Campus_db", Context.MODE_PRIVATE).edit()
                        .putString("Campus_db_value",  parent.getItemAtPosition(position).toString()).apply()
                startActivity(Intent(this, User_home::class.java))
            }
        }

    }

}