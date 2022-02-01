package sk.smorada.expenseskeeper.ui.list

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import sk.smorada.expenseskeeper.di.Injector
import sk.smorada.expenseskeeper.persistence.PersistenceProvider

class ListViewModel : ViewModel() {

    private val persistenceProvider: PersistenceProvider
        get() = Injector.appComponent.getPersistenceProvider()

    val pagingDataFlow =
        Pager(config = PagingConfig(pageSize = 10, enablePlaceholders = false)) {
            persistenceProvider.pagingSource()
        }.flow
}