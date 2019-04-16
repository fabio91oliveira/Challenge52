package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.model.repository.GoalRepository
import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import oliveira.fabio.challenge52.model.repository.WeekRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    single { GoalWithWeeksRepository(get()) }
    single { GoalRepository(get()) }
    single { WeekRepository(get()) }
}