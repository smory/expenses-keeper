package sk.smorada.expenseskeeper.di

import android.content.Context
import dagger.Component
import sk.smorada.expenseskeeper.di.module.AppModule
import sk.smorada.expenseskeeper.di.module.PersistenceModule
import sk.smorada.expenseskeeper.persistence.PersistenceProvider
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, PersistenceModule::class])
interface AppComponent {

    @Named("app")
    fun getContext(): Context

    fun getPersistenceProvider(): PersistenceProvider

}