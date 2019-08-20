package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.usecase.GoalCreateUseCase
import oliveira.fabio.challenge52.domain.usecase.GoalCreateUseCaseImpl
import oliveira.fabio.challenge52.presentation.viewmodel.GoalCreateViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private val goalCreateModule = module {
    factory<GoalCreateUseCase> { GoalCreateUseCaseImpl(get(), get()) }
    viewModel { GoalCreateViewModel(get()) }
}

private val loadGoalCreateModule by lazy { loadKoinModules(goalCreateModule) }

internal fun injectGoalCreateDependencies() = loadGoalCreateModule