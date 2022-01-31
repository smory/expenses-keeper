package sk.smorada.expenseskeerer.ui.details

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.SingleSubject
import sk.smorada.expenseskeerer.di.Injector
import sk.smorada.expenseskeerer.model.Document
import sk.smorada.expenseskeerer.persistence.PersistenceProvider
import java.io.File

class DetailsViewModel : ViewModel() {

    private val persistenceProvider: PersistenceProvider
        get() = Injector.appComponent.getPersistenceProvider()

    private val _documentLiveData = MutableLiveData<Document>()

    val documentLiveData: LiveData<Document>
        get() = _documentLiveData

    private lateinit var initialDocument: Document

    private val compositeDisposable = CompositeDisposable()

    fun iniModel(document: Document?, photoPath: String) {
        initialDocument = document?.let { doc ->
            _documentLiveData.postValue(doc)
            doc
        } ?: kotlin.run {
            val doc = createPlaceholderDocument(photoPath)
            _documentLiveData.postValue(doc)
            doc
        }
    }

    fun cancel() {
        if (initialDocument.id == 0) {  // canceled initial entry of document
            val photo = File(initialDocument.photoPath)
            if (photo.exists()) {
                photo.delete()
            }
        }
    }

    fun save(
        invoiceNum: String?,
        currency: String,
        totalPrice: Float,
        documentDate: Long,
        dueDate: Long,
        partner: String,
        note: String?,
        paid: Boolean
    ): Single<Boolean> {
        val isInvoice = !TextUtils.isEmpty(invoiceNum?.trim())
        val documentToSave = Document(
            initialDocument.id,
            if (isInvoice) invoiceNum?.trim() else null,
            currency,
            totalPrice,
            documentDate,
            if (isInvoice) dueDate else documentDate,
            initialDocument.entryDate,
            partner,
            initialDocument.photoPath,
            note,
            if (isInvoice) paid else true
        )

        var saved = false
        val completable = if (documentToSave.id == 0) {
            saved = true
            persistenceProvider.insert(documentToSave)
        } else if (documentToSave != initialDocument) {
            saved = true
            persistenceProvider.update(documentToSave)
        } else {
            Completable.complete()
        }

        val returnSubject = SingleSubject.create<Boolean>()
        return returnSubject.doOnSubscribe {
            val disposable = completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ returnSubject.onSuccess(saved) }, {
                    returnSubject.onError(it)
                })
            compositeDisposable.add(disposable)
        }
    }

    fun delete():Completable {
        return persistenceProvider.delete(initialDocument)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun createPlaceholderDocument(imagePath: String): Document {
        val nowMillis = System.currentTimeMillis()
        val dueDateMillis = nowMillis + 15 * 24 * 60 * 60 * 1000
        return Document(
            0,
            null,
            "EUR",
            0.0F,
            nowMillis,
            dueDateMillis,
            nowMillis,
            null,
            imagePath,
            null,
            false
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}