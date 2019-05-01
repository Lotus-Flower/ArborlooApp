package com.rit.matthew.arborlooapp.View.ReportList

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.R
import kotlinx.android.synthetic.main.report_list_recycler.view.*

class ReportListAdapter(val reports: ArrayList<ReportDB>, val context: Context?, val callback: BaseCallback) : RecyclerView.Adapter<ReportListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_list_recycler, parent, false))
    }

    fun updateDataSet(updatedReports: ArrayList<ReportDB>){
        reports.clear()
        reports.addAll(updatedReports)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var usageString = "Uses: " + reports[position].uses.toString()

        holder.titleTextView.text = reports[position].name as CharSequence
        holder.usageTextView.text = usageString

        holder.linearLayout.setOnClickListener({
            callback.onSuccess(mutableListOf(reports[position]))
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val titleTextView: TextView = view.text_view_title
        val usageTextView: TextView = view.text_view_usages
        val linearLayout: LinearLayout = view.linear_layout_report_recycler

    }
}