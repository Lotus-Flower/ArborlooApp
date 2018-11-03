package com.rit.matthew.arborlooapp.View.ReportDetails.ReportData

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report

import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.View.ReportList.ReportListAdapter
import kotlinx.android.synthetic.main.report_data_fragment.*

class ReportDataFragment : Fragment(), ReportDataContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportDataListAdapter
    private lateinit var presenter: ReportDataPresenter

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
        val report = activity?.intent?.getParcelableExtra("report") as Report

        recyclerView = recycler_view_report_data

        adapter = ReportDataListAdapter(report.temperature, context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    fun setEventHandlers(){

    }

    override fun displayDataList(data: ArrayList<Double>) {
        adapter.updateDataSet(data)
    }

}
