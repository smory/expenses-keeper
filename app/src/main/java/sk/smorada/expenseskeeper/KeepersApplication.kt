package sk.smorada.expenseskeeper

import android.app.Application
import com.facebook.stetho.Stetho
import sk.smorada.expenseskeeper.di.Injector

class KeepersApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}