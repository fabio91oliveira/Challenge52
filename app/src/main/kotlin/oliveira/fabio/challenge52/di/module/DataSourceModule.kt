package oliveira.fabio.challenge52.di.module

import oliveira.fabio.challenge52.persistence.datasource.GoalLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.GoalWithWeeksLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.WeekLocalDataSource
import oliveira.fabio.challenge52.persistence.datasource.impl.GoalLocalDataSourceImpl
import oliveira.fabio.challenge52.persistence.datasource.impl.GoalWithWeeksLocalDataSourceImpl
import oliveira.fabio.challenge52.persistence.datasource.impl.WeekLocalDataSourceImpl
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataSourceModule {
    private val dataSourceModule = module {
        factory<GoalLocalDataSource> { GoalLocalDataSourceImpl(get()) }
        factory<GoalWithWeeksLocalDataSource> { GoalWithWeeksLocalDataSourceImpl(get()) }
        factory<WeekLocalDataSource> { WeekLocalDataSourceImpl(get()) }


    }

    fun load() = loadKoinModules(dataSourceModule)
}