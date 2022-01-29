package sk.smorada.expenseskeerer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
open class Document(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "invoice_number") val invoiceNumber: String?,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "total_price") val totalPrice: Float,
    @ColumnInfo(name = "vat") val vat: Int,
    @ColumnInfo(name = "price_no_vat") val priceWithoutVat: Float,
    @ColumnInfo(name = "document_date") val documentDate: Long,
    @ColumnInfo(name = "due_date") val dueDate: Long?,
    @ColumnInfo(name = "entry_date") val entryDate: Long,
    @ColumnInfo(name = "partner") val partner: String?,
    @ColumnInfo(name = "photo_uri") val photoUri:  String,
    @ColumnInfo(name = "note") val note: String?,
) {
}