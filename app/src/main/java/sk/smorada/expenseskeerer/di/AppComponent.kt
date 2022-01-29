package sk.smorada.expenseskeerer.di

import android.content.Context
import dagger.Component
import sk.smorada.expenseskeerer.di.module.AppModule
import sk.smorada.expenseskeerer.di.module.PersistenceModule
import sk.smorada.expenseskeerer.persistence.PersistenceProvider
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, PersistenceModule::class])
interface AppComponent {

    @Named("app")
    fun getContext(): Context

    fun getPersistenceProvider(): PersistenceProvider

}