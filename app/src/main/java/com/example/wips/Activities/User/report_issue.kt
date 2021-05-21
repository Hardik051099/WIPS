package com.example.wips.Activities.User

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.wips.Adapters.FindUserAdapter
import com.example.wips.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import showCustomToast

class report_issue : AppCompatActivity() {

    lateinit var Report_place: SearchView
    lateinit var report_issue: EditText
    lateinit var scroll_place:ScrollView
    lateinit var submitbtn:Button
    lateinit var rep_constr:ConstraintLayout

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference


    var locationlist : ArrayList<String> = ArrayList()
    var templocationlist: MutableList<String> = ArrayList()
    lateinit var listview:ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_issue)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseRef = database.getReference()
        val user = auth.currentUser

        var arrayAdapter=ArrayAdapter(this,
        android.R.layout.simple_list_item_1, locationlist)
        listview=findViewById(R.id.placed_list)
        listview.adapter = arrayAdapter

        databaseRef.child("RoomPath").addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                locationlist.add(snapshot.key.toString())
                arrayAdapter.notifyDataSetChanged()
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


        scroll_place=findViewById(R.id.scrollplace)
        report_issue=findViewById(R.id.reporttxt)
        submitbtn=findViewById(R.id.submitbtn)
        rep_constr=findViewById(R.id.report_constraint)

        Report_place = findViewById(R.id.report_place)
        Report_place.setIconified(false)
        Report_place.clearFocus()

        Report_place.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("Submit", query)
                if (query != null) {

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                report_issue.visibility = View.INVISIBLE
                scroll_place.visibility = View.VISIBLE
                if (newText!=null){
                    templocationlist.clear()
                    locationlist.forEach {
                        if(it.toUpperCase().contains(newText.toUpperCase())){
                            templocationlist.add(it)
                        }
                    }
                    arrayAdapter =ArrayAdapter(this@report_issue,
                        android.R.layout.simple_list_item_1, templocationlist.toTypedArray())
                    listview.adapter = arrayAdapter
                }
                return true
            }
        })

        listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.d("Entered","Ofc")
            Report_place.setQuery(parent.getItemAtPosition(position).toString(),false)
            scroll_place.visibility = View.INVISIBLE
            report_issue.visibility = View.VISIBLE
        }

        submitbtn.setOnClickListener {
            Log.d("Rep Place",Report_place.query.toString())
            Log.d("Rep issue",report_issue.text.toString())
            if (user != null) {
                //id of place will come in place of Reportplace.query
                databaseRef.child("Issues").child(user.uid+"/"+Report_place.query.toString()).child("Report id").setValue(user.uid+"/"+Report_place.query.toString())
                databaseRef.child("Issues").child(user.uid+"/"+Report_place.query.toString()).child("User email").setValue(user.email)
                databaseRef.child("Issues").child(user.uid+"/"+Report_place.query.toString()).child("Description").setValue(report_issue.text.toString())
                Toast(this).showCustomToast ("Issue Submitted",true, this)
            }

        }

    }
}