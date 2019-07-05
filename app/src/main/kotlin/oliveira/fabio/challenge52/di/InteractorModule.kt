package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.interactor.GoalDetailsInteractorImpl
import org.koin.dsl.module.module

val interactorModule = module {
    single { GoalDetailsInteractorImpl(get(), get()) }
}