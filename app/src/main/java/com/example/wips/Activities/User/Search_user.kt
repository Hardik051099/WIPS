package com.example.wips.Activities.User

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wips.Adapters.FindUserAdapter
import com.example.wips.R
import com.example.wips.Utils.CustomDialogClass
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class Search_user : AppCompatActivity() {
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase

    lateinit var listview: ListView
    lateinit var searchbar: SearchView

    var namelist:  MutableList<String> = ArrayList()
    var locationlist:  MutableList<String> = ArrayList()


    var tempnamelist: MutableList<String> = ArrayList()
    var templocationlist: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        val Campus_db = getSharedPreferences("Campus_db", Context.MODE_PRIVATE).getString("Campus_db_value", "Unknown")
        //Firebase Database Instance and Referance
        db= FirebaseDatabase.getInstance()
        dbrefer=db.getReference()
        listview = findViewById(R.id.List_users)

        dbrefer.child("AllUsers").child("Users").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d("Data",p0.child("Status").getValue().toString())
                if (p0.child("Status").getValue().toString().contains("Online")){
                    if (p0.child("CurrentLocation").getValue().toString().substringBefore("/").contains(Campus_db)){
                        namelist.add(p0.child("Username").getValue().toString())
                        locationlist.add(p0.child("CurrentLocation").getValue().toString().substringAfter("/").replace("/",","))
                        var myListAdapter = FindUserAdapter(this@Search_user, namelist.toTypedArray(), locationlist.toTypedArray())
                        listview.adapter = myListAdapter
                    }
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {}
        })




        searchbar = findViewById(R.id.search_view_user)
        searchbar.setIconified(false)
        searchbar.clearFocus()


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
                    var myListAdapter = FindUserAdapter(this@Search_user, tempnamelist.toTypedArray(), templocationlist.toTypedArray())
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
                navigation_path = Campus_db+"/"+place.replace(",","/")
                startActivity(Intent(this,Navigation::class.java))
            }
        }

    }
}