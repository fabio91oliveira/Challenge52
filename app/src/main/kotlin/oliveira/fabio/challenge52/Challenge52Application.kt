package oliveira.fabio.challenge52

import android.app.Application
import oliveira.fabio.challenge52.di.KoinStarter

class Challenge52Application : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinStarter.start(this)
    }
}