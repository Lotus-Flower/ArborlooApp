package com.rit.matthew.arborlooapp.View.ReportList

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.Model.ReportData
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.View.ReportDetails.Dashboard.DashboardActivity
import kotlinx.android.synthetic.main.report_list_activity.*
import java.util.*
import kotlin.collections.ArrayList
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.rengwuxian.materialedittext.MaterialEditText
import com.rit.matthew.arborlooapp.Database.Entities.*
import com.rit.matthew.arborlooapp.Export.ExcelGenerator
import com.rit.matthew.arborlooapp.Model.ReportInfo
import com.rit.matthew.arborlooapp.Model.ReportSurvey
import com.rit.matthew.arborlooapp.Usb.UsbService
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference


class ReportListActivity : AppCompatActivity(), ReportListContract.View {

    private lateinit var presenter: ReportListPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportListAdapter

    private lateinit var report: Report
    private val reports: ArrayList<Report> = ArrayList()
    private lateinit var reportDBs: ArrayList<ReportDB>

    private var tempData = ArrayList<ReportData>()
    private var moistData = ArrayList<ReportData>()
    private var uses: Long = 0

    private lateinit var sharedPref: SharedPreferences

    private var usbService: UsbService? = null
    private lateinit var mHandler: MyHandler

    private var dataBlob: String = ""

    private val fileRequestCode: Int = 101

    private lateinit var dialogView: View

    private val usbConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            usbService = (arg1 as UsbService.UsbBinder).service
            usbService!!.setHandler(mHandler)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            usbService = null
        }
    }

    private val mUsbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                UsbService.ACTION_USB_PERMISSION_GRANTED // USB PERMISSION GRANTED
                -> Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show()
                UsbService.ACTION_USB_PERMISSION_NOT_GRANTED // USB PERMISSION NOT GRANTED
                -> Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show()
                UsbService.ACTION_NO_USB // NO USB CONNECTED
                -> Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show()
                UsbService.ACTION_USB_DISCONNECTED // USB DISCONNECTED
                -> Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show()
                UsbService.ACTION_USB_NOT_SUPPORTED // USB NOT SUPPORTED
                -> Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val reportCallback:BaseCallback = object : BaseCallback{
        override fun onSuccess(data: MutableList<*>?) {
            val reportDB = data!![0] as ReportDB

            reportDB.id?.let { presenter.getReportData(it) }
        }

        override fun onFailure() {

        }
    }

    private val deleteCallback: BaseCallback = object : BaseCallback{
        override fun onSuccess(data: MutableList<*>?) {
            val reportDB = data!![0] as ReportDB

            presenter.deleteReport(reportDB)
        }

        override fun onFailure() {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list_activity)
        setSupportActionBar(toolbar_report_list)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = ReportListPresenter(this, ReportRepository(appDB = AppDB.getInstance(this)))

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        mHandler = MyHandler(this)

        setupUI()
        setEventHandlers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.action_export -> {
                requestFilePermission()
                return true
            }
            R.id.action_delete -> {
                presenter.deleteAllReports()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        setFilters()  // Start listening notifications from UsbService
        startService(UsbService::class.java, usbConnection, null) // Start UsbService(if it was not started before) and Bind it
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mUsbReceiver)
        unbindService(usbConnection)
    }

    private fun setFilters() {
        val filter = IntentFilter()
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED)
        filter.addAction(UsbService.ACTION_NO_USB)
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED)
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED)
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED)
        registerReceiver(mUsbReceiver, filter)
    }

    private fun startService(service: Class<*>, serviceConnection: ServiceConnection, extras: Bundle?) {
        if (!UsbService.SERVICE_CONNECTED) {
            val startService = Intent(this, service)
            if (extras != null && !extras.isEmpty) {
                val keys = extras.keySet()
                for (key in keys) {
                    val extra = extras.getString(key)
                    startService.putExtra(key, extra)
                }
            }
            startService(startService)
        }
        val bindingIntent = Intent(this, service)
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun setupUI() {
        recyclerView = recycler_view_report_list
        adapter = ReportListAdapter(ArrayList(), this, reportCallback, deleteCallback)

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
        val createDialogBuilder = MaterialDialog.Builder(this)
                .title("Create Report")
                .customView(R.layout.create_report_view, true)
                .positiveText("Confirm")
                .negativeText("Cancel")
                .onPositive { dialog, _ ->
                    val reportNameEditText = dialog.view.findViewById(R.id.report_name_edit_text) as MaterialEditText

                    presenter.createReport(reportNameEditText.text.toString(), tempData, moistData, uses)

                    tempData = ArrayList()
                    moistData = ArrayList()
                    uses = 0
                }
                .onNegative { _, _ ->
                    dataBlob = ""
                }

        dialogView = createDialogBuilder.build().customView!!

        val sensorDataButton = dialogView.findViewById<Button>(R.id.button_sensor_data)
        sensorDataButton.setOnClickListener {
            dataBlob = ""
            val string = "a"

            //readSerialData("98,97,89,102,;45,54,29,76,;9,/")
            usbService!!.write(string.toByteArray())
        }

        createDialogBuilder.show()

    }

    override fun displayReportList(reports: ArrayList<ReportDB>) {

        runOnUiThread {
            reportDBs = reports
            adapter.updateDataSet(reports)
        }
    }

    override fun setData(reportDB: ReportDB) {
        report = Report.reportFromDB(reportDB)

        switchToReportDetails()
    }

    private fun switchToReportDetails(){
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("report", report)
        startActivity(intent)
    }

    private fun readSerialData(data: String){

        dataBlob = dataBlob.plus(data)

        if(dataBlob.contains("/", true)){

            Toast.makeText(this, "Sensor Data Received", Toast.LENGTH_SHORT).show()

            dialogView.findViewById<ImageView>(R.id.image_view_data).setImageResource(R.drawable.ic_check_green_24dp)

            presenter.parseSerialData(dataBlob)
        }

        if(dataBlob.contains("+", true)){
            dataBlob = ""
            Toast.makeText(this, "Sensor Sent No Data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun setSerialData(tempData: ArrayList<ReportData>, moistData: ArrayList<ReportData>, uses: Long) {
        this.tempData = tempData
        this.moistData = moistData
        this.uses = uses
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun setExcelData(reports: ArrayList<ReportDB>) {

        if(reports.isEmpty()){
            showToast("No Reports to Export")
        }

        for(report in reports){

            val workbook = (ExcelGenerator.createExcelData(Report.reportFromDB(report), ReportInfo.createInfoHashMap(report.info!!, arrayListOf<String>(*resources.getStringArray(R.array.info))), ReportSurvey.createSurveyHashMap(report.survey!!, arrayListOf<String>(*resources.getStringArray(R.array.survey)))))

            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(dir, report.name + ".xls")
            var os: FileOutputStream? = null

            try {
                os = FileOutputStream(file)
                workbook.write(os)
                showToast("Wrote files to Downloads Folder")
                Log.w("FileUtils", "Writing file$file")
            } catch (e: IOException) {
                showToast("Error writing files to Downloads Folder")
                Log.w("FileUtils", "Error writing $file", e)
            } catch (e: Exception) {
                showToast("Error writing files to Downloads Folder")
                Log.w("FileUtils", "Failed to save file", e)
            } finally {
                try {
                    if (null != os)
                        os.close()
                } catch (ex: Exception) {
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            fileRequestCode -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    presenter.getExcelData()
                } else {

                }
                return
            }
            else -> {

            }
        }
    }

    private fun showToast(data: String){
        runOnUiThread {
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestFilePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), fileRequestCode)
        }else{
            presenter.getExcelData()
        }
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private class MyHandler(activity: ReportListActivity) : Handler() {
        private val mActivity: WeakReference<ReportListActivity> = WeakReference<ReportListActivity>(activity)

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UsbService.MESSAGE_FROM_SERIAL_PORT -> {
                    val data = msg.obj as String
                    mActivity.get()!!.readSerialData(data)
                }
            }
        }
    }
}
