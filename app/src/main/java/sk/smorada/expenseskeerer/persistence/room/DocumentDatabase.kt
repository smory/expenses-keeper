package sk.smorada.expenseskeerer.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.smorada.expenseskeerer.model.Document
import sk.smorada.expenseskeerer.persistence.room.DocumentDao

@Database(entities = [Document::class], version = 1)
abstract class DocumentDatabase: RoomDatabase() {

    abstract fun documentDao(): DocumentDao

}