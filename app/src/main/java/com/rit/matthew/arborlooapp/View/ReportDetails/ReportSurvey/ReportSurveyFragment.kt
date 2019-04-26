package com.rit.matthew.arborlooapp.View.ReportDetails.ReportSurvey

import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.rit.matthew.arborlooapp.Model.Report
import com.rit.matthew.arborlooapp.Model.ReportSurvey
import com.rit.matthew.arborlooapp.R
import com.rit.matthew.arborlooapp.databinding.ReportSurveyFragmentBinding
import kotlinx.android.synthetic.main.report_survey_fragment.*

class ReportSurveyFragment : Fragment() {

    private lateinit var binding: ReportSurveyFragmentBinding
    private lateinit var survey: ReportSurvey

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


    private fun setupUI() {
        val report = activity?.intent?.getParcelableExtra("report") as Report
        binding.currentReport = report

        survey = activity?.intent?.getParcelableExtra("survey") as ReportSurvey

        Log.d("MMMM", "Person Moved: " + survey.personMove)

        setupSliders()

        survey_moved_edit_text.setText(report.survey.personMove)
        survey_material_edit_text.setText(report.survey.material)
        survey_cover_edit_text.setText(report.survey.cover)
        survey_clinic_visits_edit_text.setText(report.survey.clinic)
        survey_good_edit_text.setText(report.survey.good)
        survey_bad_edit_text.setText(report.survey.bad)
        survey_broken_edit_text.setText(report.survey.broken)
        survey_problems_edit_text.setText(report.survey.problems)

        setEventHandlers()
    }

    private fun setEventHandlers() {
        apply_survey_button.setOnClickListener {
            convertProgressToCost()
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

        survey.clean?.let { calculateProgress(it, survey_clean_slider.tickCount) }?.let { survey_clean_slider.setProgress(it) }
        survey.adult?.let { calculateProgress(it, survey_adult_slider.tickCount) }?.let { survey_adult_slider.setProgress(it) }
        survey.child?.let { calculateProgress(it, survey_child_slider.tickCount) }?.let { survey_child_slider.setProgress(it) }
        survey.calls?.let { calculateProgress(it, survey_calls_slider.tickCount) }?.let { survey_calls_slider.setProgress(it) }
        survey.move?.let { calculateProgress(it, survey_moved_slider.tickCount) }?.let { survey_moved_slider.setProgress(it) }
        survey.trees?.let { calculateProgress(it, survey_trees_slider.tickCount) }?.let { survey_trees_slider.setProgress(it) }
        survey_cost_slider.setProgress(calculateProgressFromArrayResource(survey.cost))

        setRadioButtonSurveyOptions(survey.wash, binding.surveyWashRadioGroup)
        setRadioButtonSurveyOptions(survey.coverFreq, binding.surveyCoverFreqRadioGroup)
        setRadioButtonSurveyOptions(survey.purchase, binding.surveyPurchaseRadioGroup)

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
            data.toLowerCase() == "every day" -> {
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

    private fun calculateProgressFromArrayResource(data: Int?): Float {
        val costStringList = resources.getStringArray(R.array.cost_range_array)
        val costList = arrayListOf<Int>()
        for(cost in costStringList){
            costList.add(cost.removePrefix("$").toInt())
        }

        return calculateProgress(costList.indexOf(data), costList.size)
    }

    private fun convertProgressToCost(){
        //Log.d("MMMM", survey_cost_slider.progress.toString())

        Log.d("MMMM", (binding.root.findViewById(binding.surveyWashRadioGroup.checkedRadioButtonId) as RadioButton).text.toString())
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