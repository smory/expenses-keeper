package sk.smorada.expenseskeeper.ui.list.recycler

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import sk.smorada.expenseskeeper.model.Document
import sk.smorada.expenseskeeper.ui.list.recycler.model.Separator

class ListAdapter : PagingDataAdapter<Any, RecyclerView.ViewHolder>(ListDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Document -> 0
            is Separator -> 1
            else -> throw RuntimeException("Unsupported object type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it) {
                is Document -> (holder as DocumentViewHolder).bind(it)
                is Separator -> (holder as SeparatorViewHolder).bind(it)
                else -> throw RuntimeException("Unsupported object type")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> DocumentViewHolder.create(parent)
            1 -> SeparatorViewHolder.create(parent)
            else -> throw RuntimeException("Unsupported view type $viewType")
        }
    }
}