package com.rit.matthew.arborlooapp.View.ReportList

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.View.ReportDetails.Dashboard.DashboardActivity
import com.rit.matthew.arborlooapp.View.ReportDetails.ReportInfo.ReportInfoFragment
import kotlinx.android.synthetic.main.report_list_activity.*

class ReportListActivity : AppCompatActivity(), ReportListContract.View {

    private lateinit var presenter: ReportListPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list_activity)

        presenter = ReportListPresenter(this, ReportRepository(appDB = AppDB.getInstance(this)))

        /*val repo = ReportRepository(appDB = AppDB.getInstance(this))
        val reportDB = ReportDB()

        repo.getReport(1, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "success")
            }
        })

        reportDB.name = "Arborloo 1"
        reportDB.info = "Test Information"
        reportDB.temperature = arrayListOf(6.0, 11.0, 21.0)
        reportDB.moisture = arrayListOf(40.0, 193.0, 13.0, 88.9)

        repo.insertReport(reportDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Inserted")
            }
        })

        val reportDB1 = ReportDB()

        reportDB1.name = "Arborloo 2"
        reportDB1.info = "Test Information 2"
        reportDB1.temperature = arrayListOf(5.0, 10.0, 20.0)
        reportDB1.moisture = arrayListOf(5.6, 10.8, 29.0)

        repo.insertReport(reportDB1, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Inserted")
            }
        })*/

        setupUI()
        setEventHandlers()
    }

    override fun setupUI() {
        recyclerView = recycler_view_report_list
        adapter = ReportListAdapter(ArrayList(), this, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val reportDB = data!![0] as ReportDB
                val report = Report.fromReportDB(reportDB)

                switchToReportDetails(report)
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recyclerView.adapter = adapter

        presenter.setupReportList()
    }

    override fun setEventHandlers() {

        val floatingActionButton = floating_action_button_report_list
        floatingActionButton.setOnClickListener(View.OnClickListener {
            showCreateReportDialog()
        })
    }

    private fun showCreateReportDialog() {
        MaterialDialog.Builder(this)
                .title("Create Report")
                .customView(R.layout.create_report_view, true)
                .positiveText("Confirm")
                .negativeText("Cancel")
                .onPositive(object : MaterialDialog.SingleButtonCallback{
                    override fun onClick(dialog: MaterialDialog, which: DialogAction) {

                    }

                })
                .show()
    }

    override fun displayReportList(reports: ArrayList<ReportDB>) {
        adapter.updateDataSet(reports)
    }

    fun switchToReportDetails(report: Report){
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("report", report)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

}
