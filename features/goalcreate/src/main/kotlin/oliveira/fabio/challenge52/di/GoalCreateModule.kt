package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.usecase.GoalCreateUseCase
import oliveira.fabio.challenge52.domain.usecase.impl.GoalCreateUseCaseImpl
import oliveira.fabio.challenge52.presentation.viewmodel.GoalCreateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val goalCreateModule = module {
    factory<GoalCreateUseCase> {
        GoalCreateUseCaseImpl(
            get(),
            get()
        )
    }
    viewModel { GoalCreateViewModel(get()) }
}

private val loadGoalCreateModule by lazy { loadKoinModules(goalCreateModule) }

internal fun injectGoalCreateDependencies() = loadGoalCreateModule