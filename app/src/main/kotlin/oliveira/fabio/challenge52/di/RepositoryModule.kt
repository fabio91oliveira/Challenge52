package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.model.repository.GoalWithWeeksRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    single { GoalWithWeeksRepository(get(), get()) }
}