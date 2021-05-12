package com.example.wips.Activities.Admin

import android.content.*
import android.graphics.Color
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.example.wips.Models.WifiListModel
import com.example.wips.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt.PromptStateChangeListener
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal


class Calibarate_data : AppCompatActivity() {
    lateinit var selectedAPs : ArrayList<String>
    lateinit var selectedAPsbssid : ArrayList<String>

    val MAX = 9999
    //Reference Points buttons
    lateinit var RP1 : Button
    lateinit var RP2 : Button
    lateinit var RP3 : Button
    lateinit var RP4 : Button
    lateinit var RP1_2 : Button
    lateinit var RP1_3 : Button
    lateinit var RP3_4 : Button
    lateinit var RP2_4 : Button
    lateinit var RPM : Button
    lateinit var submit : Button
    lateinit var progressBar: ProgressBar
    lateinit var dbrefer: DatabaseReference
    lateinit var db: FirebaseDatabase
    var xl1 : Int = MAX
    var xl2 : Int = MAX
    var xr1 : Int = MAX
    var xr2 : Int= MAX
    var xm : Int = MAX
    var yl1 : Int = MAX
    var yl2 : Int = MAX
    var yr1 : Int = MAX
    var yr2 : Int = MAX
    var ym : Int = MAX
    var mWifiManager : WifiManager? = null
    var wifiscanlist: List<ScanResult> = listOf()
    var wifidata: ArrayList<WifiListModel> = ArrayList()
    var copywifidata: ArrayList<WifiListModel> = ArrayList()
    var caldata : HashMap<String, HashMap<String, String>> = HashMap()
    val routerData : HashMap<String, String> = HashMap()
    var path = ""
    lateinit var preferenceManager: SharedPreferences
    lateinit var infobtn : ImageButton
    var promptflag = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibarate_data)
        selectedAPs = intent.getStringArrayListExtra("APs")
        selectedAPsbssid = intent.getStringArrayListExtra("BPs")

        for (i in selectedAPsbssid){
            routerData.put(i.substringBeforeLast("%$"), i.substringAfterLast("%$"))
        }
         path = intent.getStringExtra("path")
        Log.e("selectedaps", selectedAPs.toString() + "  " + selectedAPsbssid.toString())
        db = FirebaseDatabase.getInstance()
        dbrefer = db.getReference()
        dbrefer.child("RouterData").child(path).updateChildren(routerData as Map<String, Any>)
        //Reference Points buttons
        RP1 = findViewById(R.id.LUC)
        RP1_3 = findViewById(R.id.LM)
        RP2 = findViewById(R.id.RUC)
        RP1_2 = findViewById(R.id.UMU)
        RP3 = findViewById(R.id.LLC)
        RP2_4 = findViewById(R.id.RM)
        RP4 = findViewById(R.id.RLC)
        RP3_4 = findViewById(R.id.LMB)
        RPM = findViewById(R.id.MM)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility=View.INVISIBLE
        infobtn = findViewById(R.id.info)
        submit = findViewById(R.id.submit)
        submit.isEnabled = false
        mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        showfabPrompt()
        scanWifi()
        infobtn.setOnClickListener {
            if (promptflag) {
                promptflag = false
                val prefEditor = preferenceManager.edit()
                prefEditor.putBoolean("didShowPrompt", false)
                prefEditor.apply()
                showfabPrompt()
            }
        }
        submit.setOnClickListener {
            dbrefer.child("ReadingData").child(path).updateChildren(caldata as Map<String, Any>)
            Toast.makeText(this,"Data has been uploaded to servers successfully",Toast.LENGTH_LONG).show()
        }

    }
    fun buttonClicked(v: View){
        when(v.id){
            R.id.LLC -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    progressBar.visibility = View.INVISIBLE
                    alertDiologueCaller("RP3")
                }, 7000)

            }
            R.id.LUC -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    progressBar.visibility = View.INVISIBLE
                    alertDiologueCaller("RP1")
                }, 7000)
            }
            R.id.RUC -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    progressBar.visibility = View.INVISIBLE
                    alertDiologueCaller("RP2")
                }, 7000)
            }
            R.id.RLC -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    progressBar.visibility = View.INVISIBLE
                    alertDiologueCaller("RP4")
                }, 7000)
            }
            R.id.LM -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    progressBar.visibility = View.INVISIBLE
                    alertDiologueCaller("RP1_3")
                }, 7000)
            }
            R.id.RM -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    progressBar.visibility = View.INVISIBLE
                    alertDiologueCaller("RP2_4")
                }, 7000)
            }
            R.id.UMU -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    progressBar.visibility = View.INVISIBLE
                    alertDiologueCaller("RP1_2")
                }, 7000)
            }
            R.id.LMB -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    alertDiologueCaller("RP3_4")
                    progressBar.visibility = View.INVISIBLE
                }, 7000)
            }
            R.id.MM -> {
                progressBar.visibility = View.VISIBLE
                scanWifi()
                val handler = Handler()
                handler.postDelayed(Runnable { // Do something after 5s = 5000ms
                    progressBar.visibility = View.INVISIBLE
                    alertDiologueCaller("RPM")
                }, 7000)

            }




        }

    }


     private fun scanWifi(){

        progressBar.visibility = View.VISIBLE
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
        mWifiManager!!.startScan()
        Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show()
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
                    wifidata.add(WifiListModel(scanResult.SSID, scanResult.BSSID, level.toString()))
                   // Log.i("bssid",scanResult.BSSID)
                }
               // Log.i("wifidata succ", wifidata.toString() + wifiscanlist.toString() + mWifiManager!!.scanResults.toString())

                progressBar.visibility = View.INVISIBLE
                           }
            else {
                wifiscanlist = mWifiManager!!.scanResults
                wifidata.clear()
                for (scanResult in wifiscanlist) {
                    wifidata.add(WifiListModel(scanResult.SSID, scanResult.BSSID, scanResult.frequency.toString()))
                }
                Log.i("wifidata fail", wifidata.toString())
            }
        }
    }


    private fun alertDiologueCaller(refer: String) {
        Log.e("Alertdilogue1", "OMMM")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var refdata : HashMap<String, Int> = HashMap()
        var bssidata : HashMap<String, String> = HashMap()

        builder.setTitle("Wifi Strengths Snapshot")
        var demoselectedAPs : ArrayList<String> = ArrayList()
        // var demoselectedAPsbps : ArrayList<String> = ArrayList()
        demoselectedAPs = selectedAPs
     //   demoselectedAPsbps = selectedAPsbssid
        val wifisnap = wifidata
        val arrayAdapter = ArrayAdapter<String>(this@Calibarate_data, android.R.layout.select_dialog_item)
        for (i in wifisnap){
            if (demoselectedAPs.contains(i.wifi_name)){
                refdata.put(i.wifi_name, Integer.parseInt(i.rss))
                bssidata.put(routerData.get(i.wifi_name)!!,i.rss)
            }
        }
        for (j in demoselectedAPs){
            if (!(refdata.containsKey(j))){
                refdata.put(j, -99)
                bssidata.put(routerData.get(j)!!,(-99).toString())
            }
        }
        for (l in refdata){
            arrayAdapter.add(l.key + " -> " + l.value)
        }

        builder.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        builder.setAdapter(arrayAdapter, DialogInterface.OnClickListener { dialog, which ->

        })
        builder.setPositiveButton("Next", DialogInterface.OnClickListener { dialog, which ->
            if (refer.equals("RP3") ||refer.equals("RP1")||refer.equals("RP2")||refer.equals("RP4")   ) {
                val builderInner = AlertDialog.Builder(this@Calibarate_data)
                builderInner.setTitle("Enter (x,y) Coordinates")
                val lila1 = LinearLayout(this)
                lila1.setOrientation(LinearLayout.HORIZONTAL)
                val X = EditText(this)
                val Y = EditText(this)
                X.inputType = InputType.TYPE_CLASS_NUMBER
                Y.inputType = InputType.TYPE_CLASS_NUMBER
                lila1.addView(X)
                lila1.addView(Y)
                X.hint = "X"
                Y.hint = "Y"
                builderInner.setView(lila1)
                builderInner.setPositiveButton("Ok") { dialog, which ->


                    if(X.text.isNullOrBlank() || Y.text.isNullOrBlank()){
                        builderInner.setCancelable(false)
                        Toast.makeText(this,"Fields cannot empty",Toast.LENGTH_LONG).show()
                        return@setPositiveButton
                    }


                    when(refer){
                        "RP3" -> {
                            xl1= Integer.parseInt(X.text.toString())
                            yl1 = Integer.parseInt(Y.text.toString())
                        }
                        "RP1" -> {
                            xl2= Integer.parseInt(X.text.toString())
                            yl2 = Integer.parseInt(Y.text.toString())
                        }
                        "RP2" -> {
                            xr2= Integer.parseInt(X.text.toString())
                            yr2 = Integer.parseInt(Y.text.toString())
                        }
                        "RP4" -> {
                            xr1= Integer.parseInt(X.text.toString())
                            yr1 = Integer.parseInt(Y.text.toString())
                        }
                    }
                    builderInner.setCancelable(true)
                    dbrefer.child("Coordinates").child(path).child(refer).child("X").setValue(X.text.toString())
                    dbrefer.child("Coordinates").child(path).child(refer).child("Y").setValue(Y.text.toString())
                    caldata.put(refer,bssidata)
                    Toast.makeText(this,"Data has been calibrated successfully",Toast.LENGTH_LONG).show()
                }


                builderInner.show()

            } else if (refer.equals("RP1_3") ||refer.equals("RP1_2")||refer.equals("RP2_4")||refer.equals("RP3_4")||refer.equals("RPM") ) {
              var xmod : Float = 0.toFloat()
              var ymod : Float = 0.toFloat()
                if (xl1.equals(MAX)||xl2.equals(MAX)||yl2.equals(MAX)||yl1.equals(MAX) ||yr1.equals(MAX)||yr2.equals(MAX)||xr1.equals(MAX)||xr2.equals(MAX)){
                    Toast.makeText(this,"Please calibarate Corners first",Toast.LENGTH_LONG).show()
                    return@OnClickListener
                }
                when(refer){
                    "RP1_3" -> {
                         xmod = ((xl2+xl1) /2).toFloat()
                         ymod = ((yl2+yl1) /2).toFloat()
                    }
                    "RP1_2" -> {
                        xmod = ((xl2+xr2) /2).toFloat()
                        ymod = ((yl2+yr2) /2).toFloat()
                    }
                    "RP2_4" -> {
                        xmod = ((xr1+xr2) /2).toFloat()
                        ymod = ((yr2+yr1) /2).toFloat()
                    }
                    "RP3_4" -> {
                        xmod = ((xr1+xl1) /2).toFloat()
                        ymod = ((yr1+yl1) /2).toFloat()
                    }
                    "RPM" -> {
                        xmod = ((xl2+xr1) /2).toFloat()
                        ymod = ((yl2+yr1) /2).toFloat()
                        submit.isEnabled = true
                    }

                }
                val builderInner = AlertDialog.Builder(this@Calibarate_data)

                builderInner.setTitle("X Y Coordinates")
                builderInner.setMessage("X -> $xmod  ,  Y -> $ymod")
                builderInner.setPositiveButton("Ok") { dialog, which ->
                    dbrefer.child("Coordinates").child(path).child(refer).child("X").setValue(xmod)
                    dbrefer.child("Coordinates").child(path).child(refer).child("Y").setValue(ymod)
                    caldata.put(refer,bssidata)
                    Toast.makeText(this,"Data has been calibrated successfully",Toast.LENGTH_LONG).show()
                }
                builderInner.show()
            }
        })
        builder.show()
    }

    private fun showfabPrompt (){
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        if (!preferenceManager.getBoolean("didShowPrompt", false)){
            //RP1.setBackgroundResource(R.drawable.clingref)
            MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.info)
                    .setPrimaryText("Guide for Calibration")
                    .setSecondaryText("Consider this is blueprint of your current room. You have to calibrate each reference points by standing at the actual position of the room")
                    .setBackButtonDismissEnabled(true)
                    .setBackgroundColour(ColorUtils.setAlphaComponent(Color.parseColor("#0000FF"), 125))
                    .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            // User has pressed the prompt target
                            val prefEditor = preferenceManager.edit()
                            prefEditor.putBoolean("didShowPrompt", true)
                            prefEditor.apply()
                            showCompPrompt()
                        }
                    })
                    .show()
        }
    }

    private fun showCompPrompt() {
        //RP1.setBackgroundResource(R.drawable.round_button)
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.compass)
                .setPrimaryText("Direction")
                .setSecondaryText("Stand in the room according to this directions")
                .setBackButtonDismissEnabled(true)
                .setBackgroundColour(ColorUtils.setAlphaComponent(Color.parseColor("#0000FF"), 125))
                .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        // User has pressed the prompt target
                        showAllButtonPrompt()
                    }
                })
                .show()
    }
    private fun showAllButtonPrompt(){
        RP1.setBackgroundResource(R.drawable.clingref)
        RP1_3.setBackgroundResource(R.drawable.clingref)
        RP1_2.setBackgroundResource(R.drawable.clingref)
        RP2.setBackgroundResource(R.drawable.clingref)
        RP2_4.setBackgroundResource(R.drawable.clingref)
        RP3.setBackgroundResource(R.drawable.clingref)
        RP3_4.setBackgroundResource(R.drawable.clingref)
        RP4.setBackgroundResource(R.drawable.clingref)
        RPM.setBackgroundResource(R.drawable.clingref)

        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.LLC)
                .setPrimaryText("Reference Points")
                .setSecondaryText("This are your referance points. Each point refers to a specific position in your room. Your task is to calibrate access points values (wifi rss values) at each of this points for all the points and (x,y) position coordinates for Corner Referance points")
                .setBackButtonDismissEnabled(true)
                .setBackgroundColour(ColorUtils.setAlphaComponent(Color.parseColor("#0000FF"), 125))
                .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        // User has pressed the prompt target
                        showCornersButtonPrompt()
                    }
                })
                .show()
    }
    private fun showCornersButtonPrompt(){
        RP1_3.setBackgroundResource(R.drawable.round_button)
        RP1_2.setBackgroundResource(R.drawable.round_button)
        RP2_4.setBackgroundResource(R.drawable.round_button)
        RP3_4.setBackgroundResource(R.drawable.round_button)
        RPM.setBackgroundResource(R.drawable.round_button)

        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.LLC)
                .setPrimaryText("Corner Reference Points")
                .setSecondaryText("All 4 Corners of your room. Calibrate wifi rss values at each points and also provide position coordinates.\n Flashing point (RP3) is an Origin point, you have to calculate (x,y) coordinates of remaining 3 corner points")
                .setBackButtonDismissEnabled(true)
                .setBackgroundColour(ColorUtils.setAlphaComponent(Color.parseColor("#0000FF"), 125))
                .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        // User has pressed the prompt target
                        showCenterButtonPrompt()
                    }
                })
                .show()
    }

    private fun showCenterButtonPrompt(){
        RP1.setBackgroundResource(R.drawable.round_button)
        RP2.setBackgroundResource(R.drawable.round_button)
        RP4.setBackgroundResource(R.drawable.round_button)
        RP3.setBackgroundResource(R.drawable.round_button)
        RP1_3.setBackgroundResource(R.drawable.clingref)
        RP1_2.setBackgroundResource(R.drawable.clingref)
        RP2_4.setBackgroundResource(R.drawable.clingref)
        RP3_4.setBackgroundResource(R.drawable.clingref)
        RPM.setBackgroundResource(R.drawable.clingref)

        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.RM)
                .setPrimaryText("Center Reference Points")
                .setSecondaryText("Center Reference Points. Here you just need to calibrate wifi rss values only. Geometry will automatically calculate Position coordinates ;)")
                .setBackButtonDismissEnabled(true)
                .setBackgroundColour(ColorUtils.setAlphaComponent(Color.parseColor("#0000FF"), 125))
                .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        // User has pressed the prompt target
                        showSubmitButtonPrompt()
                        promptflag = true
                    }
                })
                .show()
    }
    private fun showSubmitButtonPrompt(){
        RP1_3.setBackgroundResource(R.drawable.round_button)
        RP1_2.setBackgroundResource(R.drawable.round_button)
        RP2_4.setBackgroundResource(R.drawable.round_button)
        RP3_4.setBackgroundResource(R.drawable.round_button)
        RPM.setBackgroundResource(R.drawable.round_button)

        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.submit)
                .setPrimaryText("Submit your calibrations")
                .setSecondaryText("After all Calibrations are done , submit your values to the server by pressing here")
                .setBackButtonDismissEnabled(true)
                .setPromptFocal(RectanglePromptFocal())
                .setBackgroundColour(ColorUtils.setAlphaComponent(Color.parseColor("#0000FF"), 125))
                .setPromptStateChangeListener(PromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        // User has pressed the prompt target
                        promptflag = true
                    }
                })
                .show()
    }




}