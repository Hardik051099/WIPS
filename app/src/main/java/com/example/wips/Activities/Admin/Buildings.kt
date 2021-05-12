package com.example.wips.Activities.Admin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wips.Adapters.CalAdapter
import com.example.wips.Interfaces.OnRecyclerItemClickListener
import com.example.wips.Models.CalModel
import com.example.wips.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception


class Buildings : AppCompatActivity(),OnRecyclerItemClickListener {
    //cal = Calibration
    lateinit var addCal : Button
    lateinit var calListRecycler : RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var adapter: CalAdapter
    lateinit var buildingList: ArrayList<CalModel>
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    lateinit var auth: FirebaseAuth
    private var newbuilding = ""
    var campus = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buildings)

        addCal = findViewById(R.id.addCal)
        calListRecycler = findViewById(R.id.calList)
        buildingList = ArrayList<CalModel>()
        db = FirebaseDatabase.getInstance()
        dbrefer = db.getReference()
        buildingList.add(CalModel("No Buildings Available", "", R.drawable.more))
        val campusname =intent.getStringExtra("campusName")
         campus = campusname
        adapter = CalAdapter(buildingList,this)
        calListRecycler.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        calListRecycler.layoutManager = layoutManager
        try {
            buildingList.clear()
            dbrefer.child("Campus").child(campus).addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    buildingList.add(CalModel(snapshot.key,"0",R.drawable.wifiicon))
                    (adapter as CalAdapter).notifyDataSetChanged()
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
        catch (e:Exception){
            buildingList.add(CalModel("No Buildings Available","",R.drawable.more))
            (adapter as CalAdapter).notifyDataSetChanged()
        }
        addCal.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Enter Building name")
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener {
                        dialog, which -> newbuilding = input.text.toString()
                   // data.add(CalModel(newbuilding,"0",R.drawable.wifiicon))
                    dbrefer.child("Campus").child(campus).child(newbuilding).setValue(newbuilding)
                    (adapter as CalAdapter).notifyDataSetChanged()

                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener {
                        dialog, which -> dialog.cancel()
                })
            builder.show()
        }


    }

    override fun onItemClick(view: View?,  position: Int) {
        //Toast.makeText(this,buildingList.get(position).name,Toast.LENGTH_SHORT).show()
        val buildingname = buildingList.get(position).name
        val intent = Intent(this@Buildings,BuildingMap::class.java)
        intent.putExtra("BuildingName",buildingname)
        intent.putExtra("campus",campus)
        startActivity(intent)
    }
}