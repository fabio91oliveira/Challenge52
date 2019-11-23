package oliveira.fabio.challenge52.di.module

import oliveira.fabio.challenge52.data.GoalRepositoryImpl
import oliveira.fabio.challenge52.data.GoalWithWeeksRepositoryImpl
import oliveira.fabio.challenge52.data.WeekRepositoryImpl
import oliveira.fabio.challenge52.domain.GoalWithWeeksRepository
import oliveira.fabio.challenge52.domain.repository.GoalRepository
import oliveira.fabio.challenge52.domain.repository.WeekRepository
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object RepositoryModule {
    private val repositoryModule = module {
        factory<GoalRepository> { GoalRepositoryImpl(get()) }
        factory<GoalWithWeeksRepository> { GoalWithWeeksRepositoryImpl(get()) }
        factory<WeekRepository> { WeekRepositoryImpl(get()) }
    }

    fun load() = loadKoinModules(repositoryModule)
}