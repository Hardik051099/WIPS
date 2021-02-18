package com.example.wips.Activities.Admin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wips.Adapters.WifiListAdapter
import com.example.wips.Models.WifiListModel
import com.example.wips.R
import com.example.wips.Utils.AppConstants
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class Newbuilding : AppCompatActivity() {

    lateinit var newbuild : Button
    lateinit var wifilist : RecyclerView
    lateinit var adapter : RecyclerView.Adapter<WifiListAdapter.MyViewHolder>
    lateinit var layoutManager: RecyclerView.LayoutManager
    var wifiscanlist: List<ScanResult> = listOf()
    lateinit var mWifiManager : WifiManager
    lateinit var wifidata: ArrayList<WifiListModel>
    var scheduleReaderHandle: ScheduledFuture<*>? = null
    lateinit var mScheduler: ScheduledExecutorService
    var initialDelay: Long = 0
    var periodReader: Long = AppConstants.FETCH_INTERVAL.toLong()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newbuilding)
        newbuild = findViewById(R.id.addap)
        wifilist = findViewById(R.id.wifilist)
        wifidata = ArrayList<WifiListModel>()
        mWifiManager = getApplicationContext().getSystemService(WIFI_SERVICE) as WifiManager
        if(!(mWifiManager.isWifiEnabled)){
            Toast.makeText(this, "Please Turn on Wifi", Toast.LENGTH_LONG).show()
            val panelIntent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)
            startActivityForResult(panelIntent, 1)
        }
        //mScheduler = Executors.newScheduledThreadPool(1)
        /* scheduleReaderHandle = mScheduler.scheduleAtFixedRate(ScheduleReader(), initialDelay, periodReader,
                TimeUnit.MILLISECONDS) */
        scanWifi()



      /*  var mWifiScanner : BroadcastReceiver = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                wifiscanlist = mWifiManager.scanResults
                Log.i("Wifi Lists", wifiscanlist.toString())
                    /* if(intent.action!!.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                    wifiscanlist = mWifiManager.scanResults
                    Log.i("Wifi Lists", wifiscanlist.toString())
                } */
            }

        }
        registerReceiver(mWifiScanner, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        mWifiManager.startScan() */


        if (wifiscanlist.isEmpty()){
            wifidata.add(WifiListModel("No Wifi Available", "Check Your Wifi Connection"))
            adapter = WifiListAdapter(wifidata)
            wifilist.adapter = adapter
            layoutManager = LinearLayoutManager(this)
            wifilist.layoutManager = layoutManager
            scanWifi()
        }
        else {
            wifidata.clear()
            wifidata = wifiscanlist as ArrayList<WifiListModel>
            adapter = WifiListAdapter(wifidata)
            wifilist.adapter = adapter
            layoutManager = LinearLayoutManager(this)
            wifilist.layoutManager = layoutManager
        }

       /* class ScheduleReader : Runnable {
            override fun run() {
                if (mWifiManager.isWifiEnabled) {
                    // get networks
                    val mResults = mWifiManager.scanResults
                    //Log.d(TAG, "New scan result: (" + mResults.size + ") networks found")
                }
            }
        } */
    }



        /**
         * Kills the periodical Thread before destroying the service
         */
        /**
         * Performs a periodical read of the WiFi scan result, then it creates a new
         * [] object containing the list of networks and finally it
         * sends it to the main activity for being displayed.
         */
    fun scanWifi(){
            mScheduler = Executors.newScheduledThreadPool(1)
            scheduleReaderHandle = mScheduler.scheduleAtFixedRate(ScheduleReader(), initialDelay, periodReader,
                    TimeUnit.MILLISECONDS)
    }
}

class ScheduleReader : Runnable {
    override fun run() {
        if (Newbuilding().mWifiManager.isWifiEnabled) {
            // get networks
             Newbuilding().wifiscanlist = Newbuilding().mWifiManager.scanResults
            Log.d("WIFI SCAN", "New scan result: (" + Newbuilding().mWifiManager.scanResults.size + ") networks found")
        }
    }
}
