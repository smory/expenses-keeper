package sk.smorada.expenseskeerer.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "invoice_number") val invoiceNumber: String?,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "total_price") val totalPrice: Float,
    @ColumnInfo(name = "document_date") val documentDate: Long,
    @ColumnInfo(name = "due_date") val dueDate: Long,
    @ColumnInfo(name = "entry_date") val entryDate: Long,
    @ColumnInfo(name = "partner") val partner: String?,
    @ColumnInfo(name = "photo_path") val photoPath:  String,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name= "paid") val paid: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    fun isInvoice() = invoiceNumber != null && invoiceNumber.isNotEmpty()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(invoiceNumber)
        parcel.writeString(currency)
        parcel.writeFloat(totalPrice)
        parcel.writeLong(documentDate)
        parcel.writeLong(dueDate)
        parcel.writeLong(entryDate)
        parcel.writeString(partner)
        parcel.writeString(photoPath)
        parcel.writeString(note)
        parcel.writeByte(if (paid) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Document> {
        override fun createFromParcel(parcel: Parcel): Document {
            return Document(parcel)
        }

        override fun newArray(size: Int): Array<Document?> {
            return arrayOfNulls(size)
        }
    }
}