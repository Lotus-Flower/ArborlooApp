package com.rit.matthew.arborlooapp.View.ReportDetails.ReportGraph

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DialogTitle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.Model.ReportData
import com.rit.matthew.arborlooapp.R
import kotlinx.android.synthetic.main.dashboard_activity.*
import kotlinx.android.synthetic.main.report_graph_fragment.*

class ReportGraphFragment : Fragment(){

    private var tempData: ArrayList<ReportData>? = ArrayList()
    private var moistData: ArrayList<ReportData>? = ArrayList()

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

        setupUI()
    }

    private fun setupUI(){
        tempData = activity?.intent?.getParcelableExtra<Report>("report")?.temperatureData
        moistData = activity?.intent?.getParcelableExtra<Report>("report")?.moistureData

        tempData?.let { getEntries(it, "Temperature") }
    }

    private fun getEntries(reportData: ArrayList<ReportData>, title: String){
        val entries: ArrayList<Entry> = ArrayList()

        for(i in reportData.indices){
            val entry: Entry = Entry()
            val entryData = reportData.get(i).data
            val entryDateTime = reportData.get(i).dateTime

            entry.y = entryData!!.toFloat()
            entry.x = entryDateTime!!.toEpochSecond().toFloat()

            entries.add(entry)
        }

        drawGraph(entries, title)
    }

    private fun drawGraph(entries: ArrayList<Entry>, title: String){
        val color = ContextCompat.getColor(view!!.getContext(), R.color.colorAccent)

        //Graph Setup
        val dataSet = LineDataSet(entries, title)
        dataSet.color = color
        dataSet.setCircleColor(color)
        val lineData = LineData(dataSet)

        report_data_graph.setData(lineData)
        report_data_graph.setScaleXEnabled(true)
        report_data_graph.getXAxis().setValueFormatter(AxisFormatter())

        val description = Description()
        description.text = "Value vs. Time"
        report_data_graph.setDescription(description)
        report_data_graph.animateX(0)
        report_data_graph.invalidate()
    }



    fun updateDataTemperature(){
        tempData?.let { getEntries(it, "Temperature") }
    }

    fun updateDataMoisture(){
        moistData?.let { getEntries(it, "Moisture") }
    }

}