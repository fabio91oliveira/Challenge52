package oliveira.fabio.challenge52.di

import android.app.Application
import oliveira.fabio.challenge52.persistence.di.DaoModule
import oliveira.fabio.challenge52.persistence.di.DataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Level.DEBUG
import org.koin.core.logger.Level.ERROR
import org.koin.core.logger.Level.INFO
import org.koin.core.logger.Level.NONE
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

object KoinStarter {
    fun start(application: Application) {
        startKoin {
            logger(object : Logger(DEBUG) {
                override fun log(
                    level: Level,
                    msg: MESSAGE
                ) {
                    when (level) {
                        DEBUG -> Timber.d(msg)
                        INFO -> Timber.i(msg)
                        ERROR -> Timber.e(msg)
                        NONE -> {
                        }
                    }
                }
            })
            androidContext(application)

            DaoModule.load()
            DataSourceModule.load()
            RepositoryModule.load()

            MainModule.load()
            GoalCreateModule.load()
            GoalDetailsModule.load()
        }
    }
}