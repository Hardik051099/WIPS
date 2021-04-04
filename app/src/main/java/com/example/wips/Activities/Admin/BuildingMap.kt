package com.example.wips.Activities.Admin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wips.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BuildingMap : AppCompatActivity() {

    lateinit var floorlistspinner:Spinner
    lateinit var roomlistspinner: Spinner

    lateinit var addfloor: Button
    lateinit var addroom: Button
    lateinit var calibrate: Button


    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    var selectedFloor = "UNKNOWN"
    var selectedRoom = "UNKNOWN"

    lateinit var buildname: TextView


    //var floorlist: MutableList<String> = mutableListOf()
  // var roomlist: MutableList<String>  = mutableListOf()
    private var path: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_map)

        floorlistspinner = findViewById(R.id.floors)
        roomlistspinner = findViewById(R.id.rooms)
        var floorlist: ArrayList<String> = ArrayList()
        addfloor = findViewById(R.id.addfloor)
        addroom = findViewById(R.id.addroom)
        calibrate = findViewById(R.id.calib)
        buildname = findViewById(R.id.buildname)
        var roomlist: ArrayList<String> = ArrayList()
        db = FirebaseDatabase.getInstance()
        dbrefer = db.getReference()
        val campus = intent.getStringExtra("campus")
        path = campus +"/"+ intent.getStringExtra("BuildingName")
        buildname.text = intent.getStringExtra("BuildingName")




        //FLOOR CODE
        val flooradapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, floorlist)
        flooradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        floorlistspinner.prompt = "Select your Floor"
        floorlistspinner.adapter = flooradapter

        //ROOM CODE
        val roomadapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomlist)
        roomadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roomlistspinner.prompt = "Select your Room"
        roomlistspinner.adapter = roomadapter


        try {
            //floorlist.clear()
            dbrefer.child("Campus").child(path).addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    floorlist.add(snapshot.key.toString())
                   // Log.i("snapshots",snapshot.toString()+" "+floorlist.toString())
                    flooradapter.notifyDataSetChanged()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {
                }


            })
        }
        catch (e: Exception){
            floorlist.add("Floors list")
            flooradapter.notifyDataSetChanged()
        }

        addfloor.setOnClickListener {
            var newfloor = ""
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Enter Floor name")
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, which ->
                        newfloor = input.text.toString()
                        // data.add(CalModel(newbuilding,"0",R.drawable.wifiicon))
                        dbrefer.child("Campus").child(path).child(newfloor).setValue(newfloor)
                        flooradapter.notifyDataSetChanged()
                    })
            builder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    })
            builder.show()
        }

        addroom.setOnClickListener {
            if (floorlist.isEmpty()){
                Toast.makeText(this,"Add Floor first",Toast.LENGTH_SHORT).show()
            }
            else {
                var newroom = ""
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Enter Room name")
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton("OK",
                        DialogInterface.OnClickListener { dialog, which ->
                            newroom = input.text.toString()
                            // data.add(CalModel(newbuilding,"0",R.drawable.wifiicon))
                            dbrefer.child("Campus").child(path).child(selectedFloor).child(newroom).setValue(newroom)
                            roomadapter.notifyDataSetChanged()
                        })
                builder.setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                        })
                builder.show()
            }
        }

        calibrate.setOnClickListener{
            if(selectedFloor.equals("UNKNOWN")||selectedRoom.equals("UNKNOWN")||selectedRoom.equals("Empty")) {
                Toast.makeText(this,"Floor or Room field is Empty",Toast.LENGTH_LONG).show()
            }
            else {
                val intent = Intent(this@BuildingMap,WifiScanner::class.java)
                intent.putExtra("path",path+"/"+selectedFloor+"/"+selectedRoom)
                startActivity(intent)
            }
        }


        fun roomspinnerfun (child: String){

           // Log.i("onitemselected",child+parent.toString())
            roomlist.clear()
            dbrefer.child("Campus").child(path).child(child).addChildEventListener(object : ChildEventListener{

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    roomlist.add(snapshot.key.toString())
                    Log.i("snapshotsroom",snapshot.toString()+" "+roomlist.toString())
                    roomadapter.notifyDataSetChanged()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("snapshotsroom",error.toString())
                }

            })
            if (roomlist.isEmpty()){
                roomlist.add("Empty")
                roomadapter.notifyDataSetChanged()
            }

        }
        floorlistspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Toast.makeText(applicationContext,"Item Selected: "+floorlist[position],Toast.LENGTH_LONG).show()
                selectedFloor = parent!!.getItemAtPosition(position).toString()
                roomlist.clear()
                roomspinnerfun(selectedFloor)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        roomlistspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRoom = parent!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }












    }
}