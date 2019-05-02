package com.rit.matthew.arborlooapp.Database.Repository

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Entities.*
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ReportRepository(private var appDB: AppDB?) {

    val TAG = "ReportRepository"

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
                        Log.d(TAG, e.toString())
                    }

                })
    }

    fun getReport(id: Long, callback: BaseCallback){

        appDB?.reportDAO()?.getReportById(id)
                ?.subscribeOn(Schedulers.newThread())
                ?.subscribe(object : SingleObserver<ReportDB> {
                    override fun onSuccess(t: ReportDB) {
                        callback.onSuccess(mutableListOf(t))
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, e.toString())
                    }

        })
    }

    fun insertReport(reportDB: ReportDB, callback: BaseCallback){

        Completable.fromCallable{ appDB?.reportDAO()?.insertReport(reportDB) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : CompletableObserver{
                    override fun onComplete() {
                        callback.onSuccess(null)
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                        Log.d(TAG, e.toString())
                    }

                })
    }

    fun updateReport(reportDB: ReportDB, callback: BaseCallback){

        Completable.fromCallable { appDB?.reportDAO()?.updateReport(reportDB) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : CompletableObserver{
                    override fun onComplete() {
                        callback.onSuccess(null)
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        Log.d(TAG, e.toString())
                    }

                })
    }

    fun deleteReport(reportDB: ReportDB, callback: BaseCallback){

        Completable.fromCallable { appDB?.reportDAO()?.deleteReport(reportDB) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : CompletableObserver{
                    override fun onComplete() {
                        callback.onSuccess(null)
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        Log.d(TAG, e.toString())
                    }

                })
    }

    fun deleteAllReports(callback: BaseCallback){

        Completable.fromCallable { appDB?.reportDAO()?.deleteAllReports() }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : CompletableObserver{
                    override fun onComplete() {
                        callback.onSuccess(null)
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        Log.d(TAG, e.toString())
                    }

                })
    }

}
