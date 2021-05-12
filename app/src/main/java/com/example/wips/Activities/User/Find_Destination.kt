package com.example.wips.Activities.User

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import com.example.wips.Adapters.FindUserAdapter
import com.example.wips.R
import com.example.wips.Utils.CustomDialogClass
import com.google.firebase.database.*

class Find_Destination : AppCompatActivity() {
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase

    var list_item:  MutableList<String> = ArrayList()
    var temp_listitem:  MutableList<String> = ArrayList()

    lateinit var listview_desti: ListView
    lateinit var searchbar_building: SearchView
    lateinit var searchbar_floor: SearchView
    lateinit var searchbar_place:SearchView

    var building_string:String = ""
    var floor_string:String = ""
    var place_string:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find__destination)

        val Campus_db = getSharedPreferences("Campus_db", Context.MODE_PRIVATE).getString("Campus_db_value", "Unknown")
        //Firebase Database Instance and Referance
        db= FirebaseDatabase.getInstance()
        dbrefer=db.getReference()
        listview_desti = findViewById(R.id.List_destination)
        searchbar_building = findViewById(R.id.search_view_building)
        searchbar_floor = findViewById(R.id.search_view_floor)
        searchbar_place = findViewById(R.id.search_view_room)

        dbrefer.child("Campus").child(Campus_db).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        list_item.add(p0.key.toString())
                        var listadapter = ArrayAdapter(
                            this@Find_Destination,
                            android.R.layout.simple_list_item_1, list_item
                        )
                        listview_desti.adapter = listadapter
            }
            override fun onChildRemoved(p0: DataSnapshot) {}
        })


        searchbar_building.setIconified(false)
        searchbar_building.clearFocus()
        searchbar_floor.setIconified(false)
        searchbar_floor.clearFocus()
        searchbar_place.setIconified(false)
        searchbar_place.clearFocus()


        searchbar_building.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("Submit", query)
                if (query != null) {

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Toast.makeText(view.context,newText,Toast.LENGTH_SHORT).show()
                if (newText != null) {
                    temp_listitem.clear()
                    list_item.forEach {
                        if (it.toUpperCase().contains(newText.toUpperCase())) {
                            temp_listitem.add(it)

                        }
                    }
                    var listadapter = ArrayAdapter(
                        this@Find_Destination,
                        android.R.layout.simple_list_item_1, temp_listitem
                    )
                    listview_desti.adapter = listadapter
                }
                return true
            }

        })
        searchbar_floor.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("Submit", query)
                if (query != null) {

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Toast.makeText(view.context,newText,Toast.LENGTH_SHORT).show()
                if (newText != null) {
                    temp_listitem.clear()
                    list_item.forEach {
                        if (it.toUpperCase().contains(newText.toUpperCase())) {
                            temp_listitem.add(it)

                        }
                    }
                    var listadapter = ArrayAdapter(
                            this@Find_Destination,
                            android.R.layout.simple_list_item_1, temp_listitem
                    )
                    listview_desti.adapter = listadapter
                }
                return true
            }

        })
        searchbar_place.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("Submit", query)
                if (query != null) {

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Toast.makeText(view.context,newText,Toast.LENGTH_SHORT).show()
                if (newText != null) {
                    temp_listitem.clear()
                    list_item.forEach {
                        if (it.toUpperCase().contains(newText.toUpperCase())) {
                            temp_listitem.add(it)

                        }
                    }
                    var listadapter = ArrayAdapter(
                            this@Find_Destination,
                            android.R.layout.simple_list_item_1, temp_listitem
                    )
                    listview_desti.adapter = listadapter
                }
                return true
            }

        })



        listview_desti.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if(building_string.isNullOrEmpty()){
                building_string = parent.getItemAtPosition(position).toString()
                searchbar_building.setQuery(building_string,true)
                searchbar_building.inputType = InputType.TYPE_NULL
                list_item.clear()
                dbrefer.child("Campus").child(Campus_db).child(building_string).addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        list_item.add(p0.key.toString())
                        var listadapter = ArrayAdapter(
                                this@Find_Destination,
                                android.R.layout.simple_list_item_1, list_item
                        )
                        listview_desti.adapter = listadapter
                    }
                    override fun onChildRemoved(p0: DataSnapshot) {}
                })
            }
            else if(floor_string.isNullOrEmpty()){
                floor_string = parent.getItemAtPosition(position).toString()
                searchbar_floor.setQuery(floor_string,true)
                searchbar_floor.inputType = InputType.TYPE_NULL
                list_item.clear()
                dbrefer.child("Campus").child(Campus_db).child(building_string).child(floor_string).addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) {}
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        list_item.add(p0.key.toString())
                        var listadapter = ArrayAdapter(
                                this@Find_Destination,
                                android.R.layout.simple_list_item_1, list_item
                        )
                        listview_desti.adapter = listadapter
                    }
                    override fun onChildRemoved(p0: DataSnapshot) {}
                })
            }
            else if(place_string.isNullOrEmpty()){
                place_string = parent.getItemAtPosition(position).toString()
                searchbar_place.inputType = InputType.TYPE_NULL
                searchbar_place.setQuery(place_string,true)

                navigation_path = Campus_db+"/"+building_string+"/"+floor_string+"/"+place_string
                startActivity(Intent(this,Navigation::class.java))
                finish()
            }

        }
    }
}