package com.rit.matthew.arborlooapp.View.ReportList

import android.app.PendingIntent
import android.content.*
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
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
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbInterface
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbEndpoint
import android.opengl.Visibility
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.widget.Button
import android.widget.Toast
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.rit.matthew.arborlooapp.Usb.UsbService
import kotlinx.android.synthetic.main.create_report_view.*
import kotlinx.android.synthetic.main.dashboard_activity.*
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.nio.charset.Charset


class ReportListActivity : AppCompatActivity(), ReportListContract.View {

    private lateinit var presenter: ReportListPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportListAdapter

    private lateinit var report: Report
    private val tempData: ArrayList<ReportData> = ArrayList()
    private val moistData: ArrayList<ReportData> = ArrayList()

    private lateinit var sharedPref: SharedPreferences

    private lateinit var usbManager: UsbManager
    private lateinit var permissionIntent: PendingIntent
    private lateinit var usbDeviceConnection: UsbDeviceConnection
    private lateinit var usbInterface: UsbInterface
    private lateinit var endPointRead: UsbEndpoint
    private var packetSize: Int = 0
    private val uiHandler = Handler()

    private lateinit var serialPort: UsbSerialDevice

    private val ACTION_USB_PERMISSION = "USB_PERMISSION"

    private var usbService: UsbService? = null
    private lateinit var mHandler: MyHandler
    private val usbConnection = object : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            usbService = (arg1 as UsbService.UsbBinder).service
            usbService!!.setHandler(mHandler)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            usbService = null
        }
    }

    private var readCallback: UsbSerialInterface.UsbReadCallback = UsbSerialInterface.UsbReadCallback { arg0 ->
        //Defining a Callback which triggers whenever data is read.
        var data: String? = null
        try {
            data = String(arg0, Charsets.UTF_8)
            writeButton(data)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    private val usbReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)

                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    usbDeviceConnection = usbManager.openDevice(device);
                    serialPort = UsbSerialDevice.createUsbSerialDevice(device, usbDeviceConnection)
                    if (serialPort.open()) {
                        serialPort.setBaudRate(9600)
                        serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8)
                        serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1)
                        serialPort.setParity(UsbSerialInterface.PARITY_NONE)
                        serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
                        serialPort.read(readCallback)

                        showData("gottee")
                    }
                }
            }
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

        presenter = ReportListPresenter(this, ReportRepository(appDB = AppDB.getInstance(this)))

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        /*permissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)

        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(usbReceiver, filter)*/

        if(!checkDataExists()){
            writeTestData()
            writeDataExists()
        }

        mHandler = MyHandler(this)

        setupUI()
        setEventHandlers()
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
            //showUsbList()
        })

        fix.setOnClickListener {
            val string = "hey"
            usbService!!.write(string.toByteArray())
        }
        fix.visibility = View.GONE
    }

    private fun showUsbList(){

        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager

        val deviceHashMap: HashMap<String, UsbDevice> = HashMap()
        for (usbDevice in usbManager.deviceList.values) {
            deviceHashMap.put(usbDevice.deviceName, usbDevice)
        }

        var selectedItem : UsbDevice

        MaterialDialog.Builder(this)
                .title("USB Devices")
                .positiveText("Confirm")
                .negativeText("Cancel")
                .items(deviceHashMap.keys)
                .itemsCallbackSingleChoice(-1, object : MaterialDialog.ListCallbackSingleChoice {
                    override fun onSelection(dialog: MaterialDialog?, itemView: View?, position: Int, text: CharSequence?): Boolean {
                        dialog!!.selectedIndex = position
                        return true
                    }
                })
                .onPositive(object : MaterialDialog.SingleButtonCallback{
                    override fun onClick(dialog: MaterialDialog, which: DialogAction) {
                        usbManager.requestPermission(deviceHashMap.get(dialog.items!!.get(dialog.selectedIndex)), permissionIntent)
                        val text = "Sending permission intent"
                        val duration = Toast.LENGTH_SHORT

                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show()
                    }
                })
                .show()
    }

    private fun showCreateReportDialog() {
        val createDialogBuilder = MaterialDialog.Builder(this)
                .title("Create Report")
                .customView(R.layout.create_report_view, true)
                .positiveText("Confirm")
                .negativeText("Cancel")
                .onPositive(object : MaterialDialog.SingleButtonCallback{
                    override fun onClick(dialog: MaterialDialog, which: DialogAction) {

                    }

                })

        val sensorDataButton = createDialogBuilder.build().customView!!.findViewById<Button>(R.id.button_sensor_data)
        sensorDataButton.setOnClickListener {
            val string = "Test Sensor Data"
            usbService!!.write(string.toByteArray())
        }

        createDialogBuilder.show()

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

    private fun switchToReportDetails(){
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("report", report)
        intent.putExtra("temp", tempData)
        intent.putExtra("moist", moistData)
        startActivity(intent)
    }

    private fun writeDataExists(){
        with (sharedPref.edit()) {
            putBoolean("hasData", true)
            apply()
        }
    }

    private fun checkDataExists() : Boolean{
        Log.d("MMMM", "Got here")
        return sharedPref.getBoolean("hasData", false)
    }

    private fun writeTestData(){
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

    private fun showData(data: String){
        Toast.makeText(this, data, Toast.LENGTH_LONG).show()
    }

    private fun writeButton(data: String){
        fix.text = data
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private class MyHandler(activity: ReportListActivity) : Handler() {
        private val mActivity: WeakReference<ReportListActivity>

        init {
            mActivity = WeakReference<ReportListActivity>(activity)
        }

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UsbService.MESSAGE_FROM_SERIAL_PORT -> {
                    val data = msg.obj as String
                    Toast.makeText(mActivity.get(), data, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
