package com.example.wips.Activities.User

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.wips.Activities.Login
import com.example.wips.Models.WifiListModel
import com.example.wips.R
import com.example.wips.Utils.CommonMapSetValues
import com.example.wips.Utils.Database
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import showCustomToast
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


var current_path:String = ""   //Globally Declared ,can be used in any activity
var navigation_path:String = ""   //Globally Declared ,can be used in any activity

class User_home : AppCompatActivity() {

    lateinit var card1_img: ImageView
    lateinit var  card1_title: TextView
    lateinit var  card1_desc: TextView
    lateinit var locateme: Button
    lateinit var card2_img: ImageView
    lateinit var  card2_title: TextView
    lateinit var  card2_desc: TextView
    lateinit var progressBar:ProgressBar
    lateinit var database: Database
    var backpress:Int = 0

    lateinit var navimg: ImageView
    lateinit var navigation_view:NavigationView
    lateinit var db:FirebaseDatabase
    lateinit var dbrefer:DatabaseReference
    lateinit var current_campus: String //This variable is having campus name use this to fetch values from db
    lateinit var current_loc: String
    lateinit var current_location: TextView
    var currentuser : String = ""
    var wifiscanlist: List<ScanResult> = listOf()
    var mWifiManager : WifiManager? = null
    lateinit var wifidata: ArrayList<WifiListModel>
    var prevValue:HashMap<String,Int> = hashMapOf()
    val serverData : HashMap<String, HashMap<String,String>> = HashMap()
    val selectedData: HashMap<String,String> = HashMap()
    val routerData: HashMap<String,Int> = HashMap()


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        current_location = findViewById(R.id.Locationtext_userhome)
        currentuser = FirebaseAuth.getInstance().currentUser!!.uid
        current_campus =
            getSharedPreferences("Campus_db", Context.MODE_PRIVATE).getString("Campus_db_value", "Unknown")
                .toString()
        current_loc = "Unknown"
        current_location.text = "Campus:- $current_campus \n Location:- $current_loc"
        //This path will get stored in variable as well as on db

        locateme= findViewById(R.id.locateme)
        progressBar = findViewById(R.id.progressBar2)
        progressBar.visibility = View.INVISIBLE
        db= FirebaseDatabase.getInstance()
        dbrefer=db.reference
        mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifidata = ArrayList<WifiListModel>()
        if(!(mWifiManager!!.isWifiEnabled)){
            Toast.makeText(this, "Please Turn on Wifi and GPS", Toast.LENGTH_LONG).show()
            val panelIntent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)
            startActivityForResult(panelIntent, 1)
        }
        scanWifi()

        //Following code is for navigation drawer ->

        navimg = findViewById<ImageView>(R.id.navimg)
        navigation_view = findViewById(R.id.navigation_view)
        val navDrawer: DrawerLayout = findViewById(R.id.drawerlayout)
        navimg.setOnClickListener {
            // If the navigation drawer is not open then open it, if its already open then close it.
            // If the navigation drawer is not open then open it, if its already open then close it.
            if (!navDrawer.isDrawerOpen(Gravity.START)) navDrawer.openDrawer(Gravity.START)
            else navDrawer.closeDrawer(Gravity.END)
        }

        navigation_view.setNavigationItemSelectedListener{
            when (it.itemId){
                R.id.action_changeplace->{
                    startActivity(Intent(this, Search_Place::class.java))
                }
                R.id.action_report ->{
                    startActivity(Intent(this, report_issue::class.java))
                }

                R.id.action_logout ->{
                    getSharedPreferences("Loggedin", Context.MODE_PRIVATE).edit()
                            .putBoolean("isLoggedin", false).apply()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this,Login::class.java))
                }

            }
            // Close the drawer
            navDrawer.closeDrawer(GravityCompat.START)
            true
        }

        //Following code is for cards in the activity ->

        val myCard1: View = findViewById(R.id.card1)
        card1_img = myCard1.findViewById(R.id.card_img) as ImageView
        card1_title = myCard1.findViewById(R.id.card_head) as TextView
        card1_desc = myCard1.findViewById(R.id.card_descriptor) as TextView
        card1_img.setImageResource(R.drawable.cardnav)
        card1_title.setText("Navigation")
        card1_desc.setText("Explore Nearby")

        val myCard2: View = findViewById(R.id.card2)
        card2_img = myCard2.findViewById(R.id.card_img) as ImageView
        card2_title = myCard2.findViewById(R.id.card_head) as TextView
        card2_desc = myCard2.findViewById(R.id.card_descriptor) as TextView
        card2_img.setImageResource(R.drawable.cardfind)
        card2_title.setText("Search")
        card2_desc.setText("Find someone")


        myCard1.setOnClickListener{
            if(current_path.isEmpty() || current_loc.equals("Unknown")){
                Toast(this).showCustomToast ("Location is empty $current_path",true, this)
                Log.e("currentpath", current_path)
            }
            else {
                Log.e("currentpath", current_path)
                startActivity(Intent(this, Navigation::class.java))
            }

        }

        myCard2.setOnClickListener{
            if(!current_path.isNullOrEmpty()){
                startActivity(Intent(this,Search_user::class.java))
            }
        }

        locateme.setOnClickListener {
            if(current_campus == "Unknown"){
                showCompPrompt()
            }
            else {
                scanWifi()
                locateme.isEnabled = false
                progressBar.visibility = View.VISIBLE
                dbrefer.child("RouterData").child(current_campus).addChildEventListener(object : ChildEventListener {
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

            }
            val handler = Handler()
            handler.postDelayed(Runnable { // Do something after 5s = 5000ms

                nearestNeighbour(wifidata,serverData)
            }, 10000)


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
        progressBar.visibility = View.INVISIBLE
        locateme.isEnabled = true

        var commonmapper = CommonMapSetValues(selectedData)

        val frequencies = commonmapper.countFrequencies()
        if(frequencies.contains("LNC"))
        {
            current_location.text = "Campus:- $current_campus \n Location:- Location not Found"
            Toast(this).showCustomToast ("Wrong Campus",false, this)
            return
        }
        if(frequencies.size == 1){
            current_loc = frequencies[0]
            current_location.text = "Campus:- $current_campus \n Location:- $current_loc"
            Log.i("currentLoc",current_loc+"__"+frequencies.toString())
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
            current_location.text = "Campus:- $current_campus \n Location:- $current_loc"
            Log.i("currentLoc2",current_loc+"__"+frequencies.toString())
        }

        findposition()
        Log.i("selectionEOK",selectedData.toString()+"__"+routerData.toString())
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

    private fun showCompPrompt() {
        //RP1.setBackgroundResource(R.drawable.round_button)
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.navimg)
            .setPrimaryText("Click Here to open Navigation bar")
            .setSecondaryText("You have to select your current Campus location first")
            .setBackButtonDismissEnabled(true)
            .setPromptStateChangeListener(MaterialTapTargetPrompt.PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                    // User has pressed the prompt target
                    showNavPrompt()
                }
            })
            .show()
    }

    private fun showNavPrompt() {
        //RP1.setBackgroundResource(R.drawable.round_button)
        MaterialTapTargetPrompt.Builder(this)
            .setTarget(R.id.action_changeplace)
            .setPrimaryText("Click Here to choose your campus location")
            .setBackButtonDismissEnabled(true)
            .setPromptFocal(RectanglePromptFocal())

            .setPromptStateChangeListener(MaterialTapTargetPrompt.PromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {

                }
            })
            .show()
    }

    fun findposition(){
        //current_campus : Search for values in this campus
        // make a variable name current_path having String in format "Campus name/Building name/Floor no./Room name"
        //Eg.
        dbrefer.child("RoomPath").child(current_loc).child("path").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                current_path = snapshot.value.toString()
                dbrefer.child("AllUsers").child("Users").child(currentuser).child("CurrentLocation").setValue(
                    current_path)
                dbrefer.child("AllUsers").child("Users").child(currentuser).child("Status").setValue(
                    "Online")

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onBackPressed() {
        backpress = backpress + 1
        if (backpress > 1) {
            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }
        else {
                Toast(this).showCustomToast("Press Back again to Exit",true,this)
        }
    }
}