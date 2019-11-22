package oliveira.fabio.challenge52

import android.app.Application
import oliveira.fabio.challenge52.di.daoModule
import oliveira.fabio.challenge52.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

class Challenge52Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Challenge52Application)
            loadKoinModules(listOf(daoModule, repositoryModule))
        }
    }
}