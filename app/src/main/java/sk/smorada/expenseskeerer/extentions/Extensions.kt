package sk.smorada.expenseskeerer.extentions

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.widget.ImageView
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Extensions {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy")

    fun Long.toFormattedDateString(): String {
        val d = Date(this)
        return dateFormat.format(d)
    }

    fun ImageView.setImageFromPath(filePath: String) {
        val file = File(filePath)
        if(file.exists() && file.isFile) {
            this.setImageBitmap(BitmapFactory.decodeFile(filePath))
        }
    }

    fun TextInputEditText.containsText(): Boolean {
        return !TextUtils.isEmpty(this.text?.trim())
    }

    fun TextInputEditText.textAsFloat(): Float {
        return this.text?.toString()?.toFloat() ?: throw NumberFormatException("Input (${this.text}) cannot be converted to float")
    }

    fun TextInputEditText.isConvertableToFloat(): Boolean {
        return try {
            this.textAsFloat()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}