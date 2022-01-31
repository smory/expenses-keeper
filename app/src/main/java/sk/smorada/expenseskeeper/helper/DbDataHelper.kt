package sk.smorada.expenseskeeper.helper

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.MaybeSubject
import sk.smorada.expenseskeeper.di.Injector
import sk.smorada.expenseskeeper.model.Document
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
                        now,
                        now,
                        now,
                        "Company $i",
                        "",
                        null,
                        true
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
                    now,
                    now,
                    now,
                    "Company $i",
                    "",
                    null,
                    true
                )
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { onSuccessAction.invoke() },
                    { Log.e(TAG, "Failed to insert a record", it) })
        }

        fun getLastDocumentMaybe(): Maybe<Document> {
            val subject = MaybeSubject.create<Document>()
            return subject.doOnSubscribe {
                Injector.appComponent.getPersistenceProvider().loadAllDocuments()
                    .subscribeOn(Schedulers.io())
                    .subscribe({ list ->
                        if ((list.isEmpty())) {
                            subject.onComplete()
                        } else {
                            subject.onSuccess(list.last())
                        }
                    }, {
                        subject.onError(it)
                    })
            }

        }
    }
}