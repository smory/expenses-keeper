package sk.smorada.expenseskeerer.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    @Named("app")
    fun provideApplicationContext(): Context = application

}