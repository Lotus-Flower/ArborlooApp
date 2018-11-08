package com.rit.matthew.arborlooapp.View.ReportList

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.rit.matthew.arborlooapp.Database.Entities.MoistureDB
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Entities.TemperatureDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.Model.ReportData
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.View.ReportDetails.Dashboard.DashboardActivity
import kotlinx.android.synthetic.main.report_list_activity.*
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import java.util.*

class ReportListActivity : AppCompatActivity(), ReportListContract.View {

    private lateinit var presenter: ReportListPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportListAdapter

    private lateinit var report: Report
    private val tempData: ArrayList<ReportData> = ArrayList()
    private val moistData: ArrayList<ReportData> = ArrayList()

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list_activity)

        presenter = ReportListPresenter(this, ReportRepository(appDB = AppDB.getInstance(this)))

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        if(!checkDataExists()){
            writeTestData()
            writeDataExists()
        }

        setupUI()
        setEventHandlers()
    }

    override fun setupUI() {
        recyclerView = recycler_view_report_list
        adapter = ReportListAdapter(ArrayList(), this, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val reportDB = data!![0] as ReportDB
                report = Report.fromReportDB(reportDB)

                presenter.getReportData(reportDB.id)
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

    override fun setData(tempData: ArrayList<ReportData>, moistData: ArrayList<ReportData>) {

        this.tempData.clear()
        this.moistData.clear()

        this.tempData.addAll(tempData)
        this.moistData.addAll(moistData)

        switchToReportDetails()
    }

    fun switchToReportDetails(){
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("report", report)
        intent.putExtra("temp", tempData)
        intent.putExtra("moist", moistData)
        startActivity(intent)
    }

    fun writeDataExists(){
        with (sharedPref.edit()) {
            putBoolean("hasData", true)
            apply()
        }
    }

    fun checkDataExists() : Boolean{
        return sharedPref.getBoolean("hasData", false)
    }

    fun writeTestData(){
        val repo = ReportRepository(appDB = AppDB.getInstance(this))
        val reportDB = ReportDB()

        reportDB.name = "Arborloo 1"
        reportDB.info = "Test Information"

        repo.insertReport(reportDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Inserted")
            }
        })

        val reportDB1 = ReportDB()

        reportDB1.name = "Arborloo 2"
        reportDB1.info = "Test Information 2"

        repo.insertReport(reportDB1, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Inserted")
            }
        })

        val rangeMin = 0.0
        val rangeMax = 100.0
        val random = Random()

        var newTempData = 0.0
        var newMoistData = 0.0
        var newDateTime: OffsetDateTime? = null
        var newReportId:Long = 1

        val points = 200

        for(i in 0..points){
            newTempData = rangeMin + (rangeMax - rangeMin) * random.nextDouble()
            newMoistData = rangeMin + (rangeMax - rangeMin) * random.nextDouble()
            newDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541404810000 + (i * 3600000)), ZoneId.systemDefault())

            if(i > (points/2)){
                newReportId = 2
            }

            var newTemp = TemperatureDB()
            var newMoist = MoistureDB()

            newTemp.data = newTempData
            newTemp.dateTime = newDateTime
            newTemp.reportId = newReportId

            newMoist.data = newMoistData
            newMoist.dateTime = newDateTime
            newMoist.reportId = newReportId

            repo.insertTemp(newTemp, object : BaseCallback{
                override fun onSuccess(data: MutableList<*>?) {

                }
            })

            repo.insertMoist(newMoist, object : BaseCallback{
                override fun onSuccess(data: MutableList<*>?) {

                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

}
