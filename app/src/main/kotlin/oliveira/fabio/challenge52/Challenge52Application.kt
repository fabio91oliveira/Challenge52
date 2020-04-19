package oliveira.fabio.challenge52

import android.app.Application
import oliveira.fabio.challenge52.di.KoinStarter
import timber.log.Timber

class Challenge52Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startTimber()
        startKoin()
    }

    private fun startTimber() {
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun startKoin() {
        KoinStarter.start(this)
    }
}