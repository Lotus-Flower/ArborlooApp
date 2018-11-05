package com.rit.matthew.arborlooapp.Database.Repository

import android.util.Log
import com.rit.matthew.arborlooapp.Base.Callback.BaseCallback
import com.rit.matthew.arborlooapp.Database.AppDatabase.AppDB
import com.rit.matthew.arborlooapp.Database.Entities.MoistureDB
import com.rit.matthew.arborlooapp.Database.Entities.ReportDB
import com.rit.matthew.arborlooapp.Database.Entities.TemperatureDB
import io.reactivex.Completable
import io.reactivex.CompletableObserver
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
                        Log.d("MMMM", e.toString())
                    }

                })
    }

    fun getReport(id: Long, callback: BaseCallback){

        Single.fromCallable<ReportDB> { appDB?.reportDAO()?.getReportById(id) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : SingleObserver<ReportDB> {
                    override fun onSuccess(t: ReportDB) {
                        //callback.onSuccess(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Log.d("MMMM", e.toString())
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
                        Log.d("MMMM", e.toString())
                    }

                })
    }

    fun getTemperatureData(reportId: Long?, callback: BaseCallback){

        Single.fromCallable{ appDB?.reportDAO()?.getTemperatureData(reportId) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : SingleObserver<MutableList<TemperatureDB>?> {
                    override fun onSuccess(t: MutableList<TemperatureDB>?) {
                        callback.onSuccess(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Log.d("MMMM", e.toString())
                    }

                })
    }

    fun getMoistureData(reportId: Long?, callback: BaseCallback){

        Single.fromCallable{ appDB?.reportDAO()?.getMoistureData(reportId) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : SingleObserver<MutableList<MoistureDB>?> {
                    override fun onSuccess(t: MutableList<MoistureDB>?) {
                        callback.onSuccess(t)
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        Log.d("MMMM", e.toString())
                    }

                })

    }

    fun insertTemp(temperatureDB: TemperatureDB, callback: BaseCallback){

        Completable.fromCallable{ appDB?.reportDAO()?.insertTemp(temperatureDB) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : CompletableObserver{
                    override fun onComplete() {
                        callback.onSuccess(null)
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("MMMM", e.toString())
                    }

                })
    }

    fun insertMoist(moistureDB: MoistureDB, callback: BaseCallback){

        Completable.fromCallable{ appDB?.reportDAO()?.insertMoist(moistureDB) }
                .subscribeOn(Schedulers.newThread())
                .subscribe(object : CompletableObserver{
                    override fun onComplete() {
                        callback.onSuccess(null)
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("MMMM", e.toString())
                    }

                })
    }

}
