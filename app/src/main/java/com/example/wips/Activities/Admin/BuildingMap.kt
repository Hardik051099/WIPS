package com.example.wips.Activities.Admin

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.wips.R
import com.google.firebase.database.*

class BuildingMap : AppCompatActivity() {

    private lateinit var floorlistspinner:Spinner
    private lateinit var roomlistspinner: Spinner

    private lateinit var addfloor: Button
    private lateinit var addroom: Button
    private lateinit var calibrate: Button


    private lateinit var dbrefer: DatabaseReference
    private lateinit var db: FirebaseDatabase
    var selectedFloor = "UNKNOWN"
    var selectedRoom = "UNKNOWN"

    private lateinit var buildname: TextView
    //lateinit var pos: Posi

    //var floorlist: MutableList<String> = mutableListOf()
  // var roomlist: MutableList<String>  = mutableListOf()
    private var path: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_map)

        floorlistspinner = findViewById(R.id.floors)
        roomlistspinner = findViewById(R.id.rooms)
        val floorlist: ArrayList<String> = ArrayList()
        addfloor = findViewById(R.id.addfloor)
        addroom = findViewById(R.id.addroom)
        calibrate = findViewById(R.id.calib)
        buildname = findViewById(R.id.buildname)
        val roomlist: ArrayList<String> = ArrayList()
        db = FirebaseDatabase.getInstance()
        dbrefer = db.reference
        val campus = intent.getStringExtra("campus")
        path = campus +"/"+ intent.getStringExtra("BuildingName")
        buildname.text = intent.getStringExtra("BuildingName")




        //FLOOR CODE
        val flooradapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, floorlist)
        flooradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        floorlistspinner.prompt = "Select your Floor"
        floorlistspinner.adapter = flooradapter

        //ROOM CODE
        val roomadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roomlist)
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
            val lila1 = LinearLayout(this)
            lila1.orientation = LinearLayout.VERTICAL
            var newfloor: String
            var newfloorno: String
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Enter Floor name and Floor Number")
            val input = EditText(this)
            val input2 = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            input2.inputType = InputType.TYPE_CLASS_PHONE
            lila1.addView(input)
            lila1.addView(input2)
            input.hint = "Floor name Without Number"
            input2.hint = "Floor number"
            builder.setView(lila1)
            builder.setPositiveButton("OK"
            ) { _, _ ->
                newfloor = input.text.toString()
                newfloorno = input2.text.toString()
                if (newfloor.isBlank() || newfloorno.isBlank()) {
                    Toast.makeText(this, "You can't leave a field empty", Toast.LENGTH_LONG).show()
                } else {
                    // data.add(CalModel(newbuilding,"0",R.drawable.wifiicon))
                    dbrefer.child("Campus").child(path).child("$newfloorno:- $newfloor")
                        .setValue(newfloor)
                    Toast.makeText(this,"Added Successfully",Toast.LENGTH_SHORT).show()
                    flooradapter.notifyDataSetChanged()
                }
            }
            builder.setNegativeButton("Cancel"
            ) { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

        addroom.setOnClickListener {
            if (floorlist.isEmpty()){
                Toast.makeText(this,"Add Floor first",Toast.LENGTH_SHORT).show()
            }
            else {
                var newroom : String
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Enter Room name")
                val input = EditText(this)
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton("OK"
                ) { _, _ ->
                    newroom = input.text.toString()
                    // data.add(CalModel(newbuilding,"0",R.drawable.wifiicon))
                    if (newroom.isBlank()) {
                        Toast.makeText(this, "You can't leave a field empty", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        dbrefer.child("Campus").child(path).child(selectedFloor).child(newroom)
                            .setValue(newroom)
                        dbrefer.child("RoomPath").child(newroom).child("path").setValue("$path/$selectedFloor/$newroom")
                        dbrefer.child("RoomPath").child(newroom).child("building").setValue(path)
                        Toast.makeText(this,"Added Successfully",Toast.LENGTH_SHORT).show()
                        roomadapter.notifyDataSetChanged()
                    }
                }
                builder.setNegativeButton("Cancel"
                ) { dialog, _ ->
                    dialog.cancel()
                }
                builder.show()
            }
        }

        calibrate.setOnClickListener{
            if(selectedFloor == "UNKNOWN" || selectedRoom == "UNKNOWN" || selectedRoom == "Empty") {
                Toast.makeText(this,"Floor or Room field is Empty",Toast.LENGTH_LONG).show()
            }
            else {
                val intent = Intent(this@BuildingMap,WifiScanner::class.java)
                intent.putExtra("path", "$path/$selectedFloor/$selectedRoom")
                startActivity(intent)
            }
        }


        fun roomspinnerfun (child: String){

           // Log.i("onitemselected",child+parent.toString())
            roomlist.clear()
            dbrefer.child("Campus").child(path).child(child).addChildEventListener(object : ChildEventListener{

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    roomlist.add(snapshot.key.toString())
                    Log.i("snapshotsroom", "$snapshot $roomlist")
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