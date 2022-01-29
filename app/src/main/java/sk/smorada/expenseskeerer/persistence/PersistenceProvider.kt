package sk.smorada.expenseskeerer.persistence

import androidx.paging.PagingSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import sk.smorada.expenseskeerer.model.Document

interface PersistenceProvider {

    fun insert(document: Document): Completable

    fun insert(documents: List<Document>): Completable

    fun update(document: Document): Completable

    fun delete(document: Document): Completable

    fun loadAllDocuments(): Single<List<Document>>

    fun pagingSource(): PagingSource<Int, Document>
}