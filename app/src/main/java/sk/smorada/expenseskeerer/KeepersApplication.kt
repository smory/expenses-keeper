package sk.smorada.expenseskeerer

import android.app.Application
import com.facebook.stetho.Stetho
import sk.smorada.expenseskeerer.di.Injector

class KeepersApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}