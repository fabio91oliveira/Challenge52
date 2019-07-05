package oliveira.fabio.challenge52

import android.app.Application
import oliveira.fabio.challenge52.di.daoModule
import oliveira.fabio.challenge52.di.interactorModule
import oliveira.fabio.challenge52.di.repositoryModule
import oliveira.fabio.challenge52.di.viewModelModule
import org.koin.android.ext.android.startKoin

class Challenge52Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(daoModule, repositoryModule, interactorModule, viewModelModule))
    }
}