package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.AddWeeksUseCase
import oliveira.fabio.challenge52.domain.usecase.impl.AddGoalUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.AddWeeksUseCaseImpl
import oliveira.fabio.challenge52.presentation.ui.activity.GoalCreateActivity
import oliveira.fabio.challenge52.presentation.viewmodel.GoalCreateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GoalCreateModule {
    private val domainModule = module {
        scope(named<GoalCreateActivity>()) {
            scoped<AddGoalUseCase> {
                AddGoalUseCaseImpl(get())
            }
            scoped<AddWeeksUseCase> {
                AddWeeksUseCaseImpl(get())
            }
        }
    }

    private val presentationModule = module {
        scope(named<GoalCreateActivity>()) {
            viewModel { GoalCreateViewModel(get(), get()) }
        }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}