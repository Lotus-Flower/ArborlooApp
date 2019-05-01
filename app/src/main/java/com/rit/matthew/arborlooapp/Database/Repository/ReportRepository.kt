package com.rit.matthew.arborlooapp.Database.Repository

import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Entities.*
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.internal.operators.observable.ObservableCache
import io.reactivex.schedulers.Schedulers
import java.util.function.BiFunction

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
                        Log.d(TAG, (t).name)
                        //callback.onSuccess(t)
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
                        Log.d(TAG, "Insert Report: " + e.toString())
                    }

                })
    }

    fun getInfo(reportId: Long?, callback: BaseCallback){

        Single.fromCallable<InfoDB>{ appDB?.reportDAO()?.getInfo(reportId) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : SingleObserver<InfoDB> {
                    override fun onSuccess(t: InfoDB) {
                        callback.onSuccess(mutableListOf(t))
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "Get Info: " + e.toString())
                        callback.onFailure()
                    }

                })

    }

    fun getSurvey(reportId: Long?, callback: BaseCallback){

        Single.fromCallable<SurveyDB>{ appDB?.reportDAO()?.getSurvey(reportId) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : SingleObserver<SurveyDB> {
                    override fun onSuccess(t: SurveyDB) {
                        callback.onSuccess(mutableListOf(t))
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, "Get Survey: " + e.toString())
                        callback.onFailure()
                    }

                })

    }

    fun insertInfo(infoDB: InfoDB, callback: BaseCallback){

        Completable.fromCallable{ appDB?.reportDAO()?.insertInfo(infoDB) }
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

    fun insertSurvey(surveyDB: SurveyDB, callback: BaseCallback){

        Completable.fromCallable{ appDB?.reportDAO()?.insertSurvey(surveyDB) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : CompletableObserver{
                    override fun onComplete() {
                        callback.onSuccess(null)
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                        Log.d(TAG, "Insert Survey: " + e.toString())
                    }

                })
    }

    fun updateInfo(infoDB: InfoDB, callback: BaseCallback){

        Completable.fromCallable{ appDB?.reportDAO()?.updateInfo(infoDB) }
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

    fun updateSurvey(surveyDB: SurveyDB, callback: BaseCallback){

        Completable.fromCallable{ appDB?.reportDAO()?.updateSurvey(surveyDB) }
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
