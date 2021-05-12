package com.example.wips.Activities.Admin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wips.Adapters.WifiListAdapter
import com.example.wips.Models.WifiListModel
import com.example.wips.R

class WifiScanner : AppCompatActivity() {

    lateinit var newbuild : Button
    lateinit var refresh : Button
    lateinit var selectall: Button

    lateinit var path: TextView

    lateinit var wifilist : RecyclerView
    lateinit var adapter : WifiListAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    var wifiscanlist: List<ScanResult> = listOf()
     var mWifiManager : WifiManager? = null
    lateinit var wifidata: ArrayList<WifiListModel>
    var prevValue:HashMap<String,Int> = hashMapOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifiscanner)
        newbuild = findViewById(R.id.addap)
        wifilist = findViewById(R.id.wifilist)
        path = findViewById(R.id.path)
        val intentpath = intent.getStringExtra("path")
        path.text = intentpath
        refresh = findViewById(R.id.refresh)
        selectall = findViewById(R.id.selectall)
        wifidata = ArrayList<WifiListModel>()
        mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if(!(mWifiManager!!.isWifiEnabled)){
            Toast.makeText(this, "Please Turn on Wifi", Toast.LENGTH_LONG).show()
            val panelIntent = Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)
            startActivityForResult(panelIntent, 1)
        }

        if (wifiscanlist.isEmpty()){
            wifidata.add(WifiListModel("No Wifi Available","0", "Check Your Wifi Connection"))
            adapter = WifiListAdapter(wifidata,WifiScanner())
            wifilist.adapter = adapter
            layoutManager = LinearLayoutManager(this)
            wifilist.layoutManager = layoutManager
            scanWifi()
        }
        refresh.setOnClickListener {
            scanWifi()
        }
        selectall.setOnClickListener {
            adapter.setSelectAll()
        }
        newbuild.setOnClickListener {
            var selectedData: ArrayList<String> = ArrayList()
            var selectedDatabssid: ArrayList<String> = ArrayList()
            for (i in wifidata){
                if (i.isSelected){
                    selectedData.add(i.wifi_name)
                    selectedDatabssid.add(i.wifi_name+"%$"+i.bssid)
                }
            }
            if(selectedData.size < 3 ){
                Toast.makeText(this,"Please Select At least 3 Wifi Access Points",Toast.LENGTH_LONG).show()
            }
            else{
            val intent = Intent(this@WifiScanner,Calibarate_data::class.java)
             intent.putStringArrayListExtra("APs",selectedData)
                intent.putStringArrayListExtra("BPs",selectedDatabssid)
                intent.putExtra("path",intentpath)


             startActivity(intent)
            }
        }
    }



    private fun scanWifi(){
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
                    adapter.notifyDataSetChanged()
                }
                Log.i("wifidata succ", wifidata.toString() + wifiscanlist.toString() + mWifiManager!!.scanResults.toString())
                adapter.notifyDataSetChanged()            }
            else {
                scanFailure()
            }
            Log.i("wifiscan ", wifiscanlist.toString())
        }
    }

    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        wifiscanlist = mWifiManager!!.scanResults
        wifidata.clear()
        for (scanResult in wifiscanlist) {
            wifidata.add(WifiListModel(scanResult.SSID, scanResult.BSSID, scanResult.frequency.toString()))
            adapter.notifyDataSetChanged()
        }
        Log.i("wifidata fail", wifidata.toString())
        adapter.notifyDataSetChanged()
    }

}
