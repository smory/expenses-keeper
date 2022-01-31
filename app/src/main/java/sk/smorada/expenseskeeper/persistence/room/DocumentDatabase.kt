package sk.smorada.expenseskeeper.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.smorada.expenseskeeper.model.Document

@Database(entities = [Document::class], version = 1)
abstract class DocumentDatabase: RoomDatabase() {

    abstract fun documentDao(): DocumentDao

}