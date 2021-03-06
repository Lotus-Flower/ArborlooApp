package com.rit.matthew.arborlooapp.View.ReportDetails.ReportInfo

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.Model.ReportInfo
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.databinding.ReportInfoFragmentBinding
import kotlinx.android.synthetic.main.report_info_fragment.*

class ReportInfoFragment : Fragment(), ReportInfoContract.View {


    private lateinit var binding: ReportInfoFragmentBinding
    private lateinit var report: Report
    private lateinit var presenter: ReportInfoContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ReportInfoPresenter(this, ReportRepository(appDB = AppDB.getInstance(context)))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.report_info_fragment, container,false)
        val view: View = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }


    private fun setupUI() {
        report = (activity?.intent?.getParcelableExtra("report") as Report)
        binding.currentReport = report

        setEventHandlers()
        setupInfoUI(report.info)
    }

    private fun setEventHandlers() {
        apply_info_button.setOnClickListener {
            report.info = constructInfoForUpdate()
            presenter.updateInfo(report)
        }
    }

    private fun setupInfoUI(info: ReportInfo?){

        info?.fullness?.toFloat()?.let { fullness_slider.setProgress(it) }
        info?.cleanliness?.toFloat()?.let { cleanliness_slider.setProgress(it) }
        info?.smell?.toFloat()?.let { smell_slider.setProgress(it) }

        setRadioButtonYesNo(info?.drainage, binding.drainageRadioGroup)
        setRadioButtonYesNo(info?.covered, binding.coverRadioGroup)
        setRadioButtonYesNo(info?.water, binding.waterRadioGroup)
        setRadioButtonYesNo(info?.soap, binding.soapRadioGroup)
        setRadioButtonYesNo(info?.wipe, binding.wipeRadioGroup)

        pests_edit_text.setText(info?.pests)
        inside_trees_edit_text.setText(info?.treesInside)
        outside_trees_edit_text.setText(info?.treesOutside)
        other_info_edit_text.setText(info?.other)

    }

    override fun setReport(report: Report) {
        activity?.intent?.putExtra("report", report)
        activity?.runOnUiThread {
            Toast.makeText(context, "Changes Applied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRadioButtonYesNo(data: Boolean?, radioGroup: RadioGroup){
        if(data == true){
            radioGroup.check(radioGroup.getChildAt(0).id)
        }else if(data == false){
            radioGroup.check(radioGroup.getChildAt(1).id)
        }
    }

    private fun radioIndexToBoolean(index: Int): Boolean{
        return index == 0
    }

    private fun constructInfoForUpdate() : ReportInfo{
        val reportInfo = ReportInfo()

        reportInfo.fullness = fullness_slider.progress
        reportInfo.cleanliness = cleanliness_slider.progress
        reportInfo.smell = smell_slider.progress
        reportInfo.drainage = radioIndexToBoolean(drainage_radio_group.indexOfChild(binding.drainageRadioGroup.findViewById(drainage_radio_group.checkedRadioButtonId)))
        reportInfo.covered = radioIndexToBoolean(cover_radio_group.indexOfChild(binding.coverRadioGroup.findViewById(cover_radio_group.checkedRadioButtonId)))
        reportInfo.water = radioIndexToBoolean(water_radio_group.indexOfChild(binding.waterRadioGroup.findViewById(water_radio_group.checkedRadioButtonId)))
        reportInfo.soap = radioIndexToBoolean(soap_radio_group.indexOfChild(binding.soapRadioGroup.findViewById(soap_radio_group.checkedRadioButtonId)))
        reportInfo.wipe = radioIndexToBoolean(wipe_radio_group.indexOfChild(binding.wipeRadioGroup.findViewById(wipe_radio_group.checkedRadioButtonId)))
        reportInfo.pests = pests_edit_text.text.toString()
        reportInfo.treesInside = inside_trees_edit_text.text.toString()
        reportInfo.treesOutside = outside_trees_edit_text.text.toString()
        reportInfo.other = other_info_edit_text.text.toString()

        return reportInfo
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            activity?.finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}