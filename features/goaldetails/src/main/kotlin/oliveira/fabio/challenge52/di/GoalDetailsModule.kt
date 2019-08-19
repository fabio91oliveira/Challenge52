package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.usecase.GoalDetailsUseCase
import oliveira.fabio.challenge52.domain.usecase.GoalDetailsUseCaseImpl
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private val goalDetailsModule = module {
    factory<GoalDetailsUseCase> { GoalDetailsUseCaseImpl(get(), get()) }
    viewModel { GoalDetailsViewModel(get()) }
}

private val loadGoalDetailsModule by lazy { loadKoinModules(goalDetailsModule) }

internal fun injectGoalDetailsDependencies() = loadGoalDetailsModule