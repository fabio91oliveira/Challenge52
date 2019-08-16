package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.usecase.GoalCreateUseCase
import oliveira.fabio.challenge52.domain.usecase.GoalCreateUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.GoalDetailsUseCase
import oliveira.fabio.challenge52.domain.usecase.GoalDetailsUseCaseImpl
import org.koin.dsl.module.module

val useCaseModule = module {
    factory<GoalDetailsUseCase> { GoalDetailsUseCaseImpl(get(), get()) }
    factory<GoalCreateUseCase> { GoalCreateUseCaseImpl(get(), get()) }
}