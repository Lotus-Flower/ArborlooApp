package com.rit.matthew.arborlooapp.View.ReportDetails.ReportSurvey

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.databinding.ReportSurveyFragmentBinding
import kotlinx.android.synthetic.main.report_info_fragment.*

class ReportSurveyFragment : Fragment() {

    private lateinit var binding: ReportSurveyFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.report_survey_fragment, container,false)
        val view: View = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }


    fun setupUI() {
        val report = activity?.intent?.getParcelableExtra("report") as Report
        binding.currentReport = report

        setEventHandlers()
    }

    fun setEventHandlers() {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            activity?.finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}