package sk.smorada.expenseskeeper.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import sk.smorada.expenseskeeper.persistence.room.DocumentDatabase
import sk.smorada.expenseskeeper.persistence.PersistenceProvider
import javax.inject.Named
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun provideRoom(@Named("app")context: Context): DocumentDatabase {
        return Room.databaseBuilder(context, DocumentDatabase::class.java, "document-database").build()
    }

    @Singleton
    @Provides
    fun provideDocumentDao(database: DocumentDatabase): PersistenceProvider = database.documentDao()
}