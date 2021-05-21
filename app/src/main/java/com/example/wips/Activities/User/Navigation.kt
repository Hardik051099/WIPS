package com.example.wips.Activities.User

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wips.Models.WifiListModel
import com.example.wips.R
import com.example.wips.Utils.CommonMapSetValues
import com.google.firebase.database.*
import showCustomToast
import java.util.*
import kotlin.collections.HashMap

class Navigation : AppCompatActivity() {
    lateinit var currentplace:String
    lateinit var navigateplace:String

    lateinit var source: TextView
    lateinit var desti:TextView
    lateinit var navbtn:Button

    lateinit var cmd_img:ImageView
    lateinit var cmd_txt:TextView
    lateinit var txtv_place:TextView
    lateinit var txtv_floor:TextView
    lateinit var txtv_building:TextView
    lateinit var db: FirebaseDatabase
    lateinit var dbrefer: DatabaseReference

    var wifiscanlist: List<ScanResult> = listOf()
    var mWifiManager : WifiManager? = null
    var current_loc:String = ""
    lateinit var wifidata: ArrayList<WifiListModel>
    var prevValue:HashMap<String,Int> = hashMapOf()
    val serverData : HashMap<String, HashMap<String,String>> = HashMap()
    val selectedData: HashMap<String,String> = HashMap()
    val routerData: HashMap<String,Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        source=findViewById(R.id.source)
        desti=findViewById(R.id.destination)
        navbtn=findViewById(R.id.navbtnnavigation)
        mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifidata = ArrayList<WifiListModel>()

        cmd_img=findViewById(R.id.cmd_img)
        cmd_txt=findViewById(R.id.cmd_txt)
        txtv_place=findViewById(R.id.txt_place)
        txtv_floor=findViewById(R.id.txt_floor)
        txtv_building=findViewById(R.id.txt_building)
        db= FirebaseDatabase.getInstance()
        dbrefer=db.reference

        currentplace= current_path
        source.setText(currentplace.substringAfter("/").replace("/",", "))
        source.setEnabled(false)

        if (!navigation_path.isNullOrEmpty()){
            navigateplace= navigation_path
            desti.setText(navigateplace.substringAfter("/").replace("/",", "))
        }
        desti.setOnClickListener {
            startActivity(Intent(this,Find_Destination::class.java))
            finish()
        }

        navbtn.setOnClickListener {
            if(desti.text.isNullOrBlank()||desti.text.equals("Destination")){
                Toast(this).showCustomToast ("Destination should not be empty",false, this)
            }
            else {
                navigate()
            }
        }
        scanWifi()
        val handler = Handler()
        handler.postDelayed(Runnable { // Do something after 5s = 5000ms
           delay()
        }, 10000)

    }
    private fun delay(){
        val ha = Handler()
        ha.postDelayed(object : Runnable {
            override fun run() {
                autolocate()
                ha.postDelayed(this, 30000)
            }
        }, 30000)
    }

    fun navigate(){
        var current_campus=currentplace.substringBefore("/")
        currentplace=currentplace.substringAfter("/")
        var current_Building=currentplace.substringBefore("/")
        currentplace=currentplace.substringAfter("/")
        var current_floor = currentplace.substringBefore(":-")
        currentplace=currentplace.substringAfter("/")
        var current_place = currentplace.substringBefore("/")

        var navigate_campus=navigateplace.substringBefore("/")
        navigateplace=navigateplace.substringAfter("/")
        var navigate_Building=navigateplace.substringBefore("/")
        navigateplace=navigateplace.substringAfter("/")
        var navigate_floor = navigateplace.substringBefore(":-")
        navigateplace=navigateplace.substringAfter("/")
        var navigate_place = navigateplace.substringBefore("/")

        txtv_place.setText(txtv_place.text.toString() + " " + current_place)
        txtv_floor.setText(txtv_floor.text.toString() + " " + current_floor)
        txtv_building.setText(txtv_building.text.toString() + " " + current_Building)

        Log.i("valuesc","$navigate_campus and $current_campus")
        if (current_campus.equals(navigate_campus)){
            if (current_Building.equals(navigate_Building)){
                if (current_floor.equals(navigate_floor)){
                    if (current_place.equals(navigate_place)){
                        //Reached
                        cmd_txt.setText("Reached destination")
                        cmd_img.setImageResource(R.drawable.arrived)
                        Log.d("Command","Reached")
                    }
                    else{
                        //Nearby
                        cmd_txt.setText("You are nearby")
                        cmd_img.setImageResource(R.drawable.near_by)
                        Log.d("Command","Nearby")
                    }
                }
                else if (current_floor.toInt() < navigate_floor.toInt()){
                    //go upstairs
                    cmd_txt.setText("Go upstairs")
                    cmd_img.setImageResource(R.drawable.upstairs)
                    Log.d("Command","Go upstairs")
                }
                else{
                    //go downstairs
                    cmd_txt.setText("Go downstairs")
                    cmd_img.setImageResource(R.drawable.downstairs)
                    Log.d("Command","Go downstairs")
                }
            }
            else{
                //Change Building
                cmd_txt.setText("Change Building")
                cmd_img.setImageResource(R.drawable.buildings)
                Log.d("Command","Change Building")
            }
        }
        else{
            //Change campus
            cmd_txt.setText("Change campus")
            cmd_img.setImageResource(R.drawable.campus)
            Log.d("Command","Change campus")
        }
    }
    private fun autolocate(){
        dbrefer.child("RouterData").child(current_path).addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("Children",snapshot.childrenCount.toString())
                for (datasnap in snapshot.children){
                    val map: HashMap<String, HashMap<String,String>> = datasnap.getValue() as HashMap<String, HashMap<String,String>>
                    Log.i("hashmap",map.toString())
                    serverData.putAll(map)
                }

            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {
                Log.i("hashmap2",serverData.toString())
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        val handler = Handler()
        handler.postDelayed({
        },5000)
    }

    private fun scanWifi(){
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
        mWifiManager!!.startScan()
        Toast(this).showCustomToast ("Scanning Wifi...",true, this)
    }
    val wifiScanReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            wifiscanlist = mWifiManager!!.scanResults
            if (success) {
                wifidata.clear()
                for (scanResult in wifiscanlist) {

                    var level = WifiManager.calculateSignalLevel(mWifiManager!!.getConnectionInfo().getRssi(),
                        scanResult.level)
                    if (level < 0){
                        prevValue.put(scanResult.SSID,level)
                        Log.i("prevvalue",prevValue.toString())
                    }
                    /*val power = (27.55-(20*(Math.log(scanResult.frequency.toDouble()))+level)/20)
                    val strength = Math.pow(10.0,power)*/
                    if(level > 0){
                        level = prevValue.get(scanResult.SSID)!!
                        Log.i("prevvalue2",prevValue.toString()+" -- $level")

                    }
                    wifidata.add(WifiListModel(scanResult.SSID,scanResult.BSSID,level.toString()))
                    Log.i("bssid",scanResult.BSSID)
                }
                Log.i("wifidata succ", wifidata.toString() + wifiscanlist.toString() + mWifiManager!!.scanResults.toString())
            }
            else {
                wifiscanlist = mWifiManager!!.scanResults
                wifidata.clear()
                for (scanResult in wifiscanlist) {
                    wifidata.add(WifiListModel(scanResult.SSID, scanResult.BSSID, scanResult.frequency.toString()))
                }
                //Log.i("wifidata fail", wifidata.toString())
            }
            //Log.i("wifiscan ", wifiscanlist.toString())
        }
    }
    private fun nearestNeighbour(wifiListModel: ArrayList<WifiListModel>,serverData:HashMap<String,HashMap<String,String>>){
    for (i in wifiListModel){
        for (j in serverData){
            for (k in j.value){
                if(j.value.containsValue(i.bssid)){
                    selectedData.put(j.value.get(i.wifi_name)!!,j.key)
                    routerData.put(i.bssid,Integer.parseInt(i.rss))
                    Log.i("selectionE",selectedData.toString()+"__"+i.wifi_name)

                }
            }
        }
    }

    var commonmapper = CommonMapSetValues(selectedData)
        Log.i("selec",selectedData.toString())
    val frequencies = commonmapper.countFrequencies()
    if(frequencies.size == 1){
        current_loc = frequencies[0]
    }
    else {
        val maxValueInMap = Collections.max<Int>(routerData.values)
        var getKey = ""
        for (i in routerData) {  // Iterate through HashMap
            if (i.value == maxValueInMap) {
                getKey = i.key
            }
        }
        current_loc = selectedData.get(getKey).toString()
        findposition()
        Log.i("currentLoc2",current_loc+"__"+frequencies.toString())
    }

    Log.i("selectionEOK",selectedData.toString()+"__"+routerData.toString())
}

    fun findposition(){
        //current_campus : Search for values in this campus
        // make a variable name current_path having String in format "Campus name/Building name/Floor no./Room name"
        //Eg.
        dbrefer.child("RoomPath").child(current_loc).child("path").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                current_path = snapshot.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}