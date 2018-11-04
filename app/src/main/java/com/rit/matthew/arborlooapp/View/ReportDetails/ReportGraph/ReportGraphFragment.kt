package com.rit.matthew.arborlooapp.View.ReportDetails.ReportGraph

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rit.matthew.arborlooapp.R
import kotlinx.android.synthetic.main.dashboard_activity.*

class ReportGraphFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.report_graph_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun updateDataTemperature(){
        Log.d("MMMM", "Temp2")
    }

    fun updateDataMoisture(){
        Log.d("MMMM", "Moist2")
    }

}