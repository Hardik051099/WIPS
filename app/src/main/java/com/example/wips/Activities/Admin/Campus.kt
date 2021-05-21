package com.example.wips.Activities.Admin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wips.Adapters.CalAdapter
import com.example.wips.Interfaces.OnRecyclerItemClickListener
import com.example.wips.Models.CalModel
import com.example.wips.R
import com.google.firebase.database.*
import java.lang.Exception

class Campus : AppCompatActivity(),OnRecyclerItemClickListener {

    lateinit var addCam : Button
    lateinit var camListRecycler: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var adapter: CalAdapter
    lateinit var campusList: ArrayList<CalModel>
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    private var campusbuilding = ""
    private val campus = "Campus"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campus)
        addCam = findViewById(R.id.addCam)
        camListRecycler = findViewById(R.id.camList)
        campusList = ArrayList<CalModel>()
        db = FirebaseDatabase.getInstance()
        dbrefer = db.getReference()
        campusList.add(CalModel("No Campus Available", "", R.drawable.more))
        adapter = CalAdapter(campusList,this)
        camListRecycler.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        camListRecycler.layoutManager = layoutManager

        try {
            campusList.clear()
            dbrefer.child(campus).addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    campusList.add(CalModel(snapshot.key,"",R.drawable.campus))
                    adapter.notifyDataSetChanged()
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
            campusList.add(CalModel("No Campus Available","",R.drawable.more))
            adapter.notifyDataSetChanged()
        }
        addCam.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Enter Campus name")
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("OK",
                    DialogInterface.OnClickListener {
                        dialog, which -> campusbuilding = input.text.toString()
                        // data.add(CalModel(newbuilding,"0",R.drawable.wifiicon))
                        dbrefer.child(campus).child(campusbuilding).setValue(campusbuilding)
                        (adapter as CalAdapter).notifyDataSetChanged()

                    })
            builder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener {
                        dialog, which -> dialog.cancel()
                    })
            builder.show()
        }


    }

    override fun onItemClick(view: View?, position: Int) {
        //Toast.makeText(this,buildingList.get(position).name,Toast.LENGTH_SHORT).show()
        val buildingname = campusList.get(position).name
        val intent = Intent(this@Campus,Buildings::class.java)
        intent.putExtra("campusName",buildingname)
        startActivity(intent)
    }
}