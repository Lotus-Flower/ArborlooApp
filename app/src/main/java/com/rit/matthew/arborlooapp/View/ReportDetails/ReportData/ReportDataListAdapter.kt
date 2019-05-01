package com.rit.matthew.arborlooapp.View.ReportDetails.ReportData

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rit.matthew.arborlooapp.Model.ReportData
import com.rit.matthew.arborlooapp.R
import kotlinx.android.synthetic.main.report_data_list_recycler.view.*
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class ReportDataListAdapter(val data: ArrayList<ReportData>?, val context: Context?) : RecyclerView.Adapter<ReportDataListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_data_list_recycler, parent, false))
    }

    fun updateDataSet(updatedData: ArrayList<ReportData>?){
        data!!.clear()
        updatedData?.let { data.addAll(it) }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (data != null) {
            return data.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = data?.get(position)?.data.toString()

        val dateTime = OffsetDateTime.ofInstant(data?.get(position)?.dateTime?.let { Instant.ofEpochSecond(it) }, ZoneId.systemDefault())
        val format = DateTimeFormatter.ISO_LOCAL_DATE
        holder.dateTimeTextView.text = dateTime.format(format)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val titleTextView: TextView = view.text_view_report_data
        val dateTimeTextView: TextView = view.text_view_report_date_time

    }
}