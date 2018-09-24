package com.rit.matthew.arborlooapp.Database.Repository

import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ReportRepository(private var appDB: AppDB?) {

    fun getReports(callback: BaseCallback){

        Single.fromCallable<MutableList<ReportDB>?> { appDB?.reportDAO()?.getReports() }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : SingleObserver<MutableList<ReportDB>?> {
                    override fun onSuccess(t: MutableList<ReportDB>?) {
                        callback.onSuccess(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }
}
