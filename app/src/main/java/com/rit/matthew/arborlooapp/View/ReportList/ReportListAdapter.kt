package com.rit.matthew.arborlooapp.View.ReportList

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.R
import kotlinx.android.synthetic.main.report_list_recycler.view.*

class ReportListAdapter(val reports: ArrayList<ReportDB>, val context: Context?, val callback: BaseCallback, val deleteCallback: BaseCallback) : RecyclerView.Adapter<ReportListAdapter.ViewHolder>(){

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

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

        val usageString = "Uses: " + reports[position].uses.toString()

        holder.titleTextView.text = reports[position].name as CharSequence
        holder.usageTextView.text = usageString

        holder.reportLayout.setOnClickListener({
            callback.onSuccess(mutableListOf(reports[position]))
        })

        holder.deleteLayout.setOnClickListener {
            deleteCallback.onSuccess(mutableListOf(reports[position]))
        }

        holder.swipeRevealLayout.close(false)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val titleTextView: TextView = view.text_view_title
        val usageTextView: TextView = view.text_view_usages
        val reportLayout: FrameLayout = view.report_layout
        val deleteLayout: FrameLayout = view.delete_layout
        val swipeRevealLayout: SwipeRevealLayout = view.swipe_reveal

    }
}