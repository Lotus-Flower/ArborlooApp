package com.rit.matthew.arborlooapp.View.ReportList

import android.content.*
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.DialogAction
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
import kotlinx.android.synthetic.main.create_report_view.*
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

    private var multipleReports = false

    private var dataBlob: String = ""

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list_activity)
        setSupportActionBar(toolbar_report_list)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        presenter = ReportListPresenter(this, ReportRepository(appDB = AppDB.getInstance(this)))

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        /*if(!checkDataExists()){
            writeTestData()
            writeDataExists()
        }*/

        mHandler = MyHandler(this)

        test_box.visibility = View.GONE

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
                testThis()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("MMMM", "Resume")
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
        adapter = ReportListAdapter(ArrayList(), this, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                val reportDB = data!![0] as ReportDB

                presenter.getReportData(reportDB)
            }
            override fun onFailure() {

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
        val createDialogBuilder = MaterialDialog.Builder(this)
                .title("Create Report")
                .customView(R.layout.create_report_view, true)
                .positiveText("Confirm")
                .negativeText("Cancel")
                .onPositive { dialog, which ->
                    val reportNameEditText = dialog.view.findViewById(R.id.report_name_edit_text) as MaterialEditText

                    presenter.createReport(reportNameEditText.text.toString(), tempData, moistData, uses)
                }
                .onNegative { dialog, which ->
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

    override fun setData(reportDB: ReportDB, info: ReportInfo?, survey: ReportSurvey?, tempData: ArrayList<ReportData>, moistData: ArrayList<ReportData>, uses: Long?) {
        report = Report.constructReportfromDB(reportDB, info, survey, tempData, moistData)

        if(!multipleReports){
            switchToReportDetails()
        }else{
            reports.add(report)

            if(reports.size == reportDBs.size){
                multipleReports = false
                getExcelData()
            }
        }
    }

    private fun switchToReportDetails(){
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("report", report)
        startActivity(intent)
    }

    private fun writeDataExists(){
        with (sharedPref.edit()) {
            putBoolean("hasData", true)
            apply()
        }
    }

    private fun checkDataExists() : Boolean{
        return sharedPref.getBoolean("hasData", false)
    }

    private fun writeTestData(){
        val rangeMin = 0.0
        val rangeMax = 100.0
        val random = Random()

        val points = 200

        val tempArray = ArrayList<ReportData>()
        val moistArray = ArrayList<ReportData>()

        val tempArray2 = ArrayList<ReportData>()
        val moistArray2 = ArrayList<ReportData>()

        for(i in 0..points){
            val newTempData = rangeMin + (rangeMax - rangeMin) * random.nextDouble()
            val newMoistData = rangeMin + (rangeMax - rangeMin) * random.nextDouble()

            val newDateLong = 1556643166L + (i * 3600L)

            val newTemp = ReportData(newTempData, newDateLong)
            val newMoist = ReportData(newMoistData, newDateLong)

            if(i > (points/2)){
                tempArray.add(newTemp)
                moistArray.add(newMoist)
            }else{
                tempArray2.add(newTemp)
                moistArray2.add(newMoist)
            }
        }

        val repo = ReportRepository(appDB = AppDB.getInstance(this))

        val reportDB = ReportDB()

        reportDB.temp = tempArray
        reportDB.moist = moistArray

        reportDB.name = "Arborloo 1"

        repo.insertReport(reportDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Inserted")
            }
            override fun onFailure() {

            }
        })

        val reportDB1 = ReportDB()

        reportDB1.temp = tempArray2
        reportDB1.moist = moistArray2

        reportDB1.name = "Arborloo 2"

        repo.insertReport(reportDB1, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "Inserted")
                writeSurveyData(repo)
                writeInfoData(repo)
            }
            override fun onFailure() {

            }
        })
    }

    private fun writeSurveyData(repo: ReportRepository){
        val surveyDB = SurveyDB()
        //= ReportSurvey(8, "yes", "water", 8, 8, "yes", 8, "father", 8, 8, "ash", "Every Day", "Everything", "Nothing", "None", "Nothing", "Yes", 300)

        surveyDB.reportId = 1
        surveyDB.clean = 0
        surveyDB.wash = "Yes"
        surveyDB.material = "Water"
        surveyDB.adult = 1
        surveyDB.child = 2
        surveyDB.clinic = "Yes"
        surveyDB.move = 3
        surveyDB.personMove = "Father"
        surveyDB.calls = 4
        surveyDB.trees = 5
        surveyDB.cover = "Ash"
        surveyDB.coverFreq = "Every Day"
        surveyDB.good = "Everything"
        surveyDB.bad = "Nothing"
        surveyDB.problems = "None"
        surveyDB.broken = "Nothing"
        surveyDB.purchase = "Yes"
        surveyDB.cost = 750

        val surveyDB2 = SurveyDB()
        //= ReportSurvey(8, "yes", "water", 8, 8, "yes", 8, "father", 8, 8, "ash", "Every Day", "Everything", "Nothing", "None", "Nothing", "Yes", 300)

        surveyDB2.reportId = 2
        surveyDB2.clean = 6
        surveyDB2.wash = "Yes"
        surveyDB2.material = "Water"
        surveyDB2.adult = 7
        surveyDB2.child = 8
        surveyDB2.clinic = "Yes"
        surveyDB2.move = 8
        surveyDB2.personMove = "Mother"
        surveyDB2.calls = 8
        surveyDB2.trees = 8
        surveyDB2.cover = "Ash"
        surveyDB2.coverFreq = "Every Day"
        surveyDB2.good = "Everything"
        surveyDB2.bad = "Nothing"
        surveyDB2.problems = "None"
        surveyDB2.broken = "Nothing"
        surveyDB2.purchase = "Yes"
        surveyDB2.cost = 1000

        repo.insertSurvey(surveyDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "inserted")
            }
            override fun onFailure() {

            }

        })

        repo.insertSurvey(surveyDB2, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "inserted")
            }
            override fun onFailure() {

            }

        })
    }

    private fun writeInfoData(repo: ReportRepository){
        val infoDB = InfoDB()

        infoDB.reportId = 1
        infoDB.fullness = 1
        infoDB.drainage = true
        infoDB.cleanliness = 2
        infoDB.covered = true
        infoDB.pests = "None"
        infoDB.smell = 3
        infoDB.water = true
        infoDB.soap = true
        infoDB.wipe = true
        infoDB.treesInside = "Some"
        infoDB.treesOutside = "Some"
        infoDB.other = "None"

        val infoDB2 = InfoDB()

        infoDB2.reportId = 2
        infoDB2.fullness = null
        infoDB2.drainage = true
        infoDB2.cleanliness = 4
        infoDB2.covered = true
        infoDB2.pests = "None"
        infoDB2.smell = 5
        infoDB2.water = true
        infoDB2.soap = true
        infoDB2.wipe = true
        infoDB2.treesInside = "Some"
        infoDB2.treesOutside = "Some"
        infoDB2.other = "None"

        repo.insertInfo(infoDB, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "inserted")
            }
            override fun onFailure() {

            }

        })

        repo.insertInfo(infoDB2, object : BaseCallback{
            override fun onSuccess(data: MutableList<*>?) {
                Log.d("MMMM", "inserted")
            }
            override fun onFailure() {

            }

        })
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

    private fun testThis(){
        //val infoArray = arrayListOf<String>(*resources.getStringArray(R.array.info))
        //val surveyArray = arrayListOf<String>(*resources.getStringArray(R.array.survey))

        multipleReports = true

        /*for(reportDB in reportDBs){
            Log.d("MMMM", "repDB" + reportDB.name)

            presenter.getReportData(reportDB)
        }*/

        //presenter.reportRepository.getReport(2, object : BaseCallback{
            /*override fun onSuccess(data: MutableList<*>?) {

            }

            override fun onFailure() {

            }

        })*/
    }

    private fun getExcelData(){
        for(report in reports){
            val workbook = (ExcelGenerator.createExcelData(report, ReportInfo.createInfoHashMap(report.info!!, arrayListOf<String>(*resources.getStringArray(R.array.info))), ReportSurvey.createSurveyHashMap(report.survey!!, arrayListOf<String>(*resources.getStringArray(R.array.survey)))))

            val file = File(getExternalFilesDir(null), report.name)
            var os: FileOutputStream? = null

            try {
                os = FileOutputStream(file)
                workbook.write(os)
                Log.w("FileUtils", "Writing file$file")
            } catch (e: IOException) {
                Log.w("FileUtils", "Error writing $file", e)
            } catch (e: Exception) {
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
                    //Toast.makeText(mActivity.get(), data, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

}
