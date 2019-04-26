package com.rit.matthew.arborlooapp.View.ReportDetails.ReportInfo

import com.rit.matthew.arborlooapp.Database.Entities.InfoDB
import com.rit.matthew.arborlooapp.Model.ReportInfo

interface ReportInfoContract {

    interface View{

        fun setInfo(info: ReportInfo)

        fun setupInfoUI(info: ReportInfo)

    }

    interface Presenter{

        fun updateInfo(infoDB: InfoDB)

    }

}