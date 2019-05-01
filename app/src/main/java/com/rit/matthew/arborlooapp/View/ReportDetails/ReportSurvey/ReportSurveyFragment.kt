package com.rit.matthew.arborlooapp.View.ReportDetails.ReportSurvey

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Repository.ReportRepository
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.Model.ReportSurvey
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.databinding.ReportSurveyFragmentBinding
import kotlinx.android.synthetic.main.report_survey_fragment.*

class ReportSurveyFragment : Fragment(), ReportSurveyContract.View {

    private lateinit var binding: ReportSurveyFragmentBinding

    private lateinit var report: Report

    private lateinit var presenter: ReportSurveyContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ReportSurveyPresenter(this, ReportRepository(appDB = AppDB.getInstance(context)))
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


    private fun setupUI() {
        report = activity?.intent?.getParcelableExtra("report") as Report
        binding.currentReport = report

        setupSliders()

        survey_moved_edit_text.setText(report.survey?.personMove)
        survey_material_edit_text.setText(report.survey?.material)
        survey_cover_edit_text.setText(report.survey?.cover)
        survey_clinic_visits_edit_text.setText(report.survey?.clinic)
        survey_good_edit_text.setText(report.survey?.good)
        survey_bad_edit_text.setText(report.survey?.bad)
        survey_broken_edit_text.setText(report.survey?.broken)
        survey_problems_edit_text.setText(report.survey?.problems)

        setEventHandlers()
    }

    private fun setEventHandlers() {
        apply_survey_button.setOnClickListener {
            report.survey = constructSurveyForUpdate()
            presenter.updateSurvey(report)
        }
    }

    private fun setupSliders(){
        survey_clean_slider.setIndicatorTextFormat("\${TICK_TEXT}")
        survey_adult_slider.setIndicatorTextFormat("\${TICK_TEXT}")
        survey_child_slider.setIndicatorTextFormat("\${TICK_TEXT}")
        survey_calls_slider.setIndicatorTextFormat("\${TICK_TEXT}")
        survey_moved_slider.setIndicatorTextFormat("\${TICK_TEXT}")
        survey_trees_slider.setIndicatorTextFormat("\${TICK_TEXT}")
        survey_cost_slider.setIndicatorTextFormat("\${TICK_TEXT}")

        report.survey?.clean?.let { calculateProgress(it, survey_clean_slider.tickCount) }?.let { survey_clean_slider.setProgress(it) }
        report.survey?.adult?.let { calculateProgress(it, survey_adult_slider.tickCount) }?.let { survey_adult_slider.setProgress(it) }
        report.survey?.child?.let { calculateProgress(it, survey_child_slider.tickCount) }?.let { survey_child_slider.setProgress(it) }
        report.survey?.calls?.let { calculateProgress(it, survey_calls_slider.tickCount) }?.let { survey_calls_slider.setProgress(it) }
        report.survey?.move?.let { calculateProgress(it, survey_moved_slider.tickCount) }?.let { survey_moved_slider.setProgress(it) }
        report.survey?.trees?.let { calculateProgress(it, survey_trees_slider.tickCount) }?.let { survey_trees_slider.setProgress(it) }
        survey_cost_slider.setProgress(calculateProgressFromArrayResource(report.survey?.cost))

        setRadioButtonSurveyOptions(report.survey?.wash, binding.surveyWashRadioGroup)
        setRadioButtonSurveyOptions(report.survey?.coverFreq, binding.surveyCoverFreqRadioGroup)
        setRadioButtonSurveyOptions(report.survey?.purchase, binding.surveyPurchaseRadioGroup)

    }

    private fun setRadioButtonSurveyOptions(data: String?, radioGroup: RadioGroup){
        if(data == null){
            return
        }
        when {
            data.toLowerCase() == "yes" -> {
                radioGroup.check(radioGroup.getChildAt(0).id)
            }
            data.toLowerCase() == "no" -> {
                radioGroup.check(radioGroup.getChildAt(1).id)
            }
            data.toLowerCase() == "every use" -> {
                radioGroup.check(radioGroup.getChildAt(0).id)
            }
            data.toLowerCase() == "daily" -> {
                radioGroup.check(radioGroup.getChildAt(1).id)
            }
            else -> {
                radioGroup.check(radioGroup.getChildAt(2).id)
            }
        }
    }

    private fun calculateProgress(data: Int, max: Int): Float {
        return ((data.toFloat()) * 100F)/ (max.toFloat() - 1F)
    }

    private fun calculateValue(progress: Int, max: Int) : Int {
        return (((progress * (max - 1F))/100F).toInt())
    }

    private fun getValueFromArrayResource(progress: Int): Int{
        val costStringList = resources.getStringArray(R.array.cost_range_array)
        val index: Int = ((progress * 5) / 100)

        return costStringList.get(index).toInt()
    }

    private fun calculateProgressFromArrayResource(data: Int?): Float {
        val costStringList = resources.getStringArray(R.array.cost_range_array)
        val costList = arrayListOf<Int>()
        for(cost in costStringList){
            costList.add(cost.toInt())
        }

        return calculateProgress(costList.indexOf(data), costList.size)
    }

    private fun constructSurveyForUpdate() : ReportSurvey{

        val reportSurvey = ReportSurvey()

        reportSurvey.clean = calculateValue(survey_clean_slider.progress, survey_clean_slider.tickCount)
        reportSurvey.adult = calculateValue(survey_adult_slider.progress, survey_adult_slider.tickCount)
        reportSurvey.child = calculateValue(survey_child_slider.progress, survey_child_slider.tickCount)
        reportSurvey.calls = calculateValue(survey_calls_slider.progress, survey_calls_slider.tickCount)
        reportSurvey.trees = calculateValue(survey_trees_slider.progress, survey_trees_slider.tickCount)
        reportSurvey.move = calculateValue(survey_moved_slider.progress, survey_moved_slider.tickCount)

        reportSurvey.personMove = survey_moved_edit_text.text.toString()
        reportSurvey.material = survey_material_edit_text.text.toString()
        reportSurvey.cover = survey_cover_edit_text.text.toString()
        reportSurvey.clinic = survey_clinic_visits_edit_text.text.toString()
        reportSurvey.good = survey_good_edit_text.text.toString()
        reportSurvey.bad = survey_bad_edit_text.text.toString()
        reportSurvey.broken = survey_broken_edit_text.text.toString()
        reportSurvey.problems = survey_problems_edit_text.text.toString()

        reportSurvey.wash = (binding.root.findViewById(binding.surveyWashRadioGroup.checkedRadioButtonId) as RadioButton).text.toString()
        reportSurvey.coverFreq = (binding.root.findViewById(binding.surveyCoverFreqRadioGroup.checkedRadioButtonId) as RadioButton).text.toString()
        reportSurvey.purchase = (binding.root.findViewById(binding.surveyPurchaseRadioGroup.checkedRadioButtonId) as RadioButton).text.toString()

        reportSurvey.cost = getValueFromArrayResource(survey_cost_slider.progress)

        return reportSurvey

    }

    override fun setReport(report: Report) {
        activity?.intent?.putExtra("report", report)
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