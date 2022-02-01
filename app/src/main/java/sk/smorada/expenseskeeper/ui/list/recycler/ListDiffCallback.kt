package sk.smorada.expenseskeeper.ui.list.recycler

import androidx.recyclerview.widget.DiffUtil
import sk.smorada.expenseskeeper.model.Document
import sk.smorada.expenseskeeper.ui.list.recycler.model.Separator

class ListDiffCallback : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Document && newItem is Document) {
            oldItem.id == newItem.id
        } else oldItem is Separator && newItem is Separator
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is Document && newItem is Document) {
            oldItem == newItem
        } else oldItem is Separator && newItem is Separator
    }

}