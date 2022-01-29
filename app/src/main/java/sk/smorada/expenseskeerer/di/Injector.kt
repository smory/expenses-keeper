package sk.smorada.expenseskeerer.di

import android.app.Application
import sk.smorada.expenseskeerer.di.module.AppModule

class Injector {

    companion object {

        lateinit var appComponent: AppComponent

        fun init(application: Application) {
            appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .build()
        }

    }

}