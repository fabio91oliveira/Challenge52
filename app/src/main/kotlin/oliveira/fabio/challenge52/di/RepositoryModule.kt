package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { GoalWithWeeksRepository(get()) }
    factory { GoalRepository(get()) }
    factory { WeekRepository(get()) }
}