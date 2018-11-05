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

class ReportListActivity : AppCompatActivity(), ReportListContract.View {

    private lateinit var presenter: ReportListPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportListAdapter

    private lateinit var report: Report
    private val tempData: ArrayList<ReportData> = ArrayList()
    private val moistData: ArrayList<ReportData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list_activity)

        presenter = ReportListPresenter(this, ReportRepository(appDB = AppDB.getInstance(this)))

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

        val temperatureDB1 = TemperatureDB()

        temperatureDB1.data = 90.0
        temperatureDB1.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541404810000), ZoneId.systemDefault())
        temperatureDB1.reportId = 1

        repo.insertTemp(temperatureDB1, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val temperatureDB2 = TemperatureDB()

        temperatureDB2.data = 91.0
        temperatureDB2.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541408410000), ZoneId.systemDefault())
        temperatureDB2.reportId = 1

        repo.insertTemp(temperatureDB2, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val temperatureDB3 = TemperatureDB()

        temperatureDB3.data = 92.0
        temperatureDB3.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541412010000), ZoneId.systemDefault())
        temperatureDB3.reportId = 1

        repo.insertTemp(temperatureDB3, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val temperatureDB4 = TemperatureDB()

        temperatureDB4.data = 93.0
        temperatureDB4.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541404810000), ZoneId.systemDefault())
        temperatureDB4.reportId = 2

        repo.insertTemp(temperatureDB4, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val temperatureDB5 = TemperatureDB()

        temperatureDB5.data = 54.0
        temperatureDB5.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541408410000), ZoneId.systemDefault())
        temperatureDB5.reportId = 2

        repo.insertTemp(temperatureDB5, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val temperatureDB6 = TemperatureDB()

        temperatureDB6.data = 102.0
        temperatureDB6.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541412010000), ZoneId.systemDefault())
        temperatureDB6.reportId = 2

        repo.insertTemp(temperatureDB6, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val moistureDB1 = MoistureDB()

        moistureDB1.data = 6.0
        moistureDB1.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541404810000), ZoneId.systemDefault())
        moistureDB1.reportId = 1

        repo.insertMoist(moistureDB1, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val moistureDB2 = MoistureDB()

        moistureDB2.data = 7.0
        moistureDB2.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541408410000), ZoneId.systemDefault())
        moistureDB2.reportId = 1

        repo.insertMoist(moistureDB2, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val moistureDB3 = MoistureDB()

        moistureDB3.data = 8.0
        moistureDB3.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541404810000), ZoneId.systemDefault())
        moistureDB3.reportId = 2

        repo.insertMoist(moistureDB3, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

        val moistureDB4 = MoistureDB()

        moistureDB4.data = 9.0
        moistureDB4.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(1541408410000), ZoneId.systemDefault())
        moistureDB4.reportId = 2

        repo.insertMoist(moistureDB4, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {

            }
        })

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

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

}
