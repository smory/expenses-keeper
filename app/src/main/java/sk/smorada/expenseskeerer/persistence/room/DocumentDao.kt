package sk.smorada.expenseskeerer.persistence.room

import androidx.paging.PagingSource
import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import sk.smorada.expenseskeerer.model.Document
import sk.smorada.expenseskeerer.persistence.PersistenceProvider

@Dao
interface DocumentDao: PersistenceProvider {

    @Insert
    override fun insert(document: Document): Completable

    @Insert
    override fun insert(documents: List<Document>): Completable

    @Update
    override fun update(document: Document): Completable

    @Delete
    override fun delete(document: Document): Completable

    @Query("SELECT * FROM documents")
    override fun loadAllDocuments(): Single<List<Document>>

    @Query("SELECT * FROM documents")
    override fun pagingSource(): PagingSource<Int, Document>

}