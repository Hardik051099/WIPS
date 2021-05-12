package com.example.wips.Utils

import android.util.Log
import com.example.wips.Models.Building
import com.example.wips.Models.Map

lateinit var map: ArrayList<Map>
lateinit var building: ArrayList<Building>

class Database {

    //Initialization of arraylists
   init {
        map = ArrayList<Map>()
        building = ArrayList<Building>()

   }

    fun add_place(Place_name:String, Place_id: String){
        map.add(Map(Place_name,Place_id))
        Log.d("Place",map[0].place_name.toString())
        Log.d("Place1",map[0].place_id.toString())
    }

    fun add_building(Place_id: String,Building_name:String,Building_id:String){

        building.add(Building(Place_id,Building_name,Building_id))
        Log.d("Plac2", building[0].building_name.toString())
        Log.d("Place3", building[0].buildin_id.toString())
    }
}