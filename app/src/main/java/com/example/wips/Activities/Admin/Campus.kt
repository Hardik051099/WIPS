package com.example.wips.Activities.Admin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wips.Activities.Login
import com.example.wips.Adapters.CalAdapter
import com.example.wips.Interfaces.OnRecyclerItemClickListener
import com.example.wips.Models.CalModel
import com.example.wips.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
    var backpress :Int = 0



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

    override fun onBackPressed() {
        backpress = backpress + 1
        if (backpress > 1) {
            /*val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)*/
            val alertbox = AlertDialog.Builder(this)
                .setMessage("Do you want to Log Out?")
                .setPositiveButton("Yes") { arg0, arg1 ->
                    getSharedPreferences("Loggedin", Context.MODE_PRIVATE).edit()
                        .putBoolean("isLoggedin", false).apply()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, Login::class.java))
                    // do something when the button is clicked
                    finish()
                    //close();
                }
                .setNegativeButton(
                    "No"
                ) // do something when the button is clicked
                { arg0, arg1 -> }
                .show()
        }
        else{
            Toast.makeText(this,"Press Back again to Log Out",Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.admin_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.admin_logout -> {
                getSharedPreferences("Loggedin", Context.MODE_PRIVATE).edit()
                    .putBoolean("isLoggedin", false).apply()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, Login::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}