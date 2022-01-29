package sk.smorada.expenseskeerer.helper

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import sk.smorada.expenseskeerer.di.Injector
import sk.smorada.expenseskeerer.model.Document
import kotlin.random.Random

class DbDataHelper {

    companion object {

        private const val TAG = "DbDataHelper"

        fun insertMultipleTestRecords(nuOfDocuments: Int, onSuccessAction: () -> Unit) {
            val list = mutableListOf<Document>()
            for (i in 1..nuOfDocuments) {
                val now = System.currentTimeMillis()
                list.add(
                    Document(
                        0,
                        "IN$i",
                        "EUR",
                        i * 2f,
                        20,
                        i.toFloat(),
                        now,
                        now,
                        now,
                        "Company $i",
                        "",
                        null
                    )
                )
            }
            Injector.appComponent.getPersistenceProvider().insert(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { onSuccessAction.invoke() },
                    { Log.e(TAG, "Failed to insert records", it) })
        }

        fun insertSingleTestRecords(onSuccessAction: () -> Unit) {
            val now = System.currentTimeMillis()
            val i = Random.nextInt()
            Injector.appComponent.getPersistenceProvider().insert(
                Document(
                    0,
                    "IN$i",
                    "EUR",
                    i * 2f,
                    20,
                    i.toFloat(),
                    now,
                    now,
                    now,
                    "Company $i",
                    "",
                    null
                )
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { onSuccessAction.invoke() },
                    { Log.e(TAG, "Failed to insert a record", it) })
        }
    }
}