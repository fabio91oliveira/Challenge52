package oliveira.fabio.challenge52.di

import android.app.Application
import oliveira.fabio.challenge52.persistence.di.DaoModule
import oliveira.fabio.challenge52.persistence.di.DataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

object KoinStarter {
    fun start(application: Application) {
        startKoin {
            logger(object : Logger(Level.DEBUG) {
                override fun log(
                    level: Level,
                    msg: MESSAGE
                ) {
                    when (level) {
                        Level.DEBUG -> Timber.d(msg)
                        Level.INFO -> Timber.i(msg)
                        Level.ERROR -> Timber.e(msg)
                    }
                }
            })
            androidContext(application)

            DaoModule.load()
            DataSourceModule.load()
            RepositoryModule.load()

            GoalHomeModule.load()
            GoalCreateModule.load()
            GoalDetailsModule.load()
        }
    }
}