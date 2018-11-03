package com.rit.matthew.arborlooapp.View.ReportDetails.Dashboard

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.util.Log
import com.rit.matthew.arborlooapp.View.ReportDetails.ReportData.ReportDataFragment
import com.rit.matthew.arborlooapp.View.ReportDetails.ReportGraph.ReportGraphFragment
import com.rit.matthew.arborlooapp.View.ReportDetails.ReportInfo.ReportInfoFragment

class DashboardPagerAdapter(private val fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private final val INFO: Int = 0
    private final val DATA: Int = 1
    private final val GRAPH: Int = 2

    private final val numTabs: Int = 3

    override fun getItem(position: Int): Fragment? {
        //Determines what tab we're on, if data exists for that tab, pass it as parameters
        return when (position) {
            INFO -> {
                ReportInfoFragment()
            }
            DATA -> {
                ReportDataFragment()
            }
            GRAPH -> {
                ReportGraphFragment()
            }
            else -> null
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Info"
            1 -> "Data"
            2 -> "Graph"
            else -> {
                return ""
            }
        }
    }

    override fun getCount(): Int {
        return numTabs
    }

}