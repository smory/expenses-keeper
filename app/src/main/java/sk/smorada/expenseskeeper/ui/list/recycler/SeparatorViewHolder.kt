package sk.smorada.expenseskeeper.ui.list.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sk.smorada.expenseskeeper.databinding.ItemSeparatorBinding
import sk.smorada.expenseskeeper.ui.list.recycler.model.Separator

class SeparatorViewHolder(binding: ItemSeparatorBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(separator: Separator) {
        // no need to do anything here
    }

    companion object {
        fun create(parent: ViewGroup): SeparatorViewHolder {
            val binding =
                ItemSeparatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SeparatorViewHolder(binding)
        }
    }
}