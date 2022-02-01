package sk.smorada.expenseskeeper.ui.list.recycler

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.smorada.expenseskeeper.Consts
import sk.smorada.expenseskeeper.databinding.ItemDocumentBinding
import sk.smorada.expenseskeeper.extentions.Extensions.toFormattedDateString
import sk.smorada.expenseskeeper.model.Document
import sk.smorada.expenseskeeper.ui.details.DetailsActivity

class DocumentViewHolder(private val binding: ItemDocumentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(document: Document) {
        binding.tvPartner.text = document.partner
        binding.tvTotal.text = formatPrice(document.totalPrice, document.currency)
        binding.tvDueDate.text = document.dueDate.toFormattedDateString()
        binding.ivPaid.visibility = if (document.paid) View.VISIBLE else View.GONE

        val additionalInfoText = buildAdditionalInfoText(document.invoiceNumber, document.note)
        if (TextUtils.isEmpty(additionalInfoText)) {
            binding.tvAdditionalInfo.visibility = View.GONE
        } else {
            binding.tvAdditionalInfo.visibility = View.VISIBLE
            binding.tvAdditionalInfo.text = additionalInfoText
        }

        binding.root.setOnClickListener {
            val context = it.context
            context.startActivity(
                Intent(
                    context,
                    DetailsActivity::class.java
                ).apply { putExtra(Consts.DOCUMENT_PARAM, document) })
        }
    }

    private fun buildAdditionalInfoText(invoiceNo: String?, note: String?): String {
        val builder = StringBuilder()
            .append(if (TextUtils.isEmpty(invoiceNo)) "" else "No: $invoiceNo")
        if (!TextUtils.isEmpty(note)) {
            builder.append(if (builder.isNotEmpty()) ", $note" else "$note")
        }
        return builder.toString()
    }

    private fun formatPrice(price: Float, currency: String): String {
        return String.format("%.2f %s", price, currency)
    }

    companion object {

        fun create(parent: ViewGroup): DocumentViewHolder {
            val binding =
                ItemDocumentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DocumentViewHolder(binding)
        }

    }
}