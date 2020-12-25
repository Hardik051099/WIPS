package com.example.wips.Activities.Admin

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wips.Adapters.CalAdapter
import com.example.wips.Models.CalModel
import com.example.wips.R

class Buildings : AppCompatActivity() {
    //cal = Calibration
    lateinit var addCal : Button
    lateinit var calList : RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CalAdapter.ViewHolder>? = null
    lateinit var data: ArrayList<CalModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buildings)

        addCal = findViewById(R.id.addCal)
        calList = findViewById(R.id.calList)
        data = ArrayList<CalModel>()
        data.add(CalModel("Building1" , "1" , R.drawable.wifiicon))
        data.add(CalModel("Building2" , "2" , R.drawable.wifiicon))
        data.add(CalModel("Building3" , "3" , R.drawable.wifiicon))
        data.add(CalModel("Building4" , "4" , R.drawable.wifiicon))
        data.add(CalModel("Building5" , "5" , R.drawable.wifiicon))
        data.add(CalModel("Building6" , "6" , R.drawable.wifiicon))
        data.add(CalModel("Building7" , "7" , R.drawable.wifiicon))
        adapter = CalAdapter(data)
        calList.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        calList.layoutManager = layoutManager

    }
}