package com.rit.matthew.arborlooapp.View.ReportDetails.Dashboard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.MenuItem
import com.rit.matthew.arborlooapp.R
import kotlinx.android.synthetic.main.dashboard_activity.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = "Report Details"

        setupUI()
    }

    private fun setupUI(){

        val adapter: DashboardPagerAdapter = DashboardPagerAdapter(supportFragmentManager)
        pager.adapter = adapter

        tab_layout.setupWithViewPager(pager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

}
