package com.rit.matthew.arborlooapp.View.ReportDetails.ReportData

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.Model.ReportData

import com.rit.matthew.arborlooapp.R
import kotlinx.android.synthetic.main.report_data_fragment.*

class ReportDataFragment : Fragment(), ReportDataContract.View{

    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ReportDataListAdapter
    private lateinit var presenter: ReportDataPresenter

    private lateinit var report:Report
    private var tempData: ArrayList<ReportData>? = ArrayList()
    private var moistData: ArrayList<ReportData>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ReportDataPresenter(this, ReportRepository(appDB = AppDB.getInstance(context)))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.report_data_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    fun setupUI(){
        report = activity?.intent?.getParcelableExtra("report") as Report
        tempData = activity?.intent?.getParcelableArrayListExtra("temp")
        moistData = activity?.intent?.getParcelableArrayListExtra("moist")

        recyclerView = recycler_view_report_data

        val copyTempList: ArrayList<ReportData>? = ArrayList()
        tempData?.let { copyTempList?.addAll(it) }

        adapter = ReportDataListAdapter(copyTempList, context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        setEventHandlers()
    }

    fun setEventHandlers(){

    }

    fun updateDataTemperature(){
        text_view_report_data_title.text = resources.getString(R.string.temperature)
        displayDataList(tempData)
    }

    fun updateDataMoisture(){
        text_view_report_data_title.text = resources.getString(R.string.moisture)
        displayDataList(moistData)
    }

    override fun displayDataList(data: ArrayList<ReportData>?) {
        adapter.updateDataSet(data)
    }

}
