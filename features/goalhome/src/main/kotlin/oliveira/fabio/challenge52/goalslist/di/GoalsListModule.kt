package oliveira.fabio.challenge52.goalslist.di

import oliveira.fabio.challenge52.goalslist.presentation.viewmodel.GoalsListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val goalsListModule = module {
    viewModel { GoalsListViewModel(get(), get(), get()) }
}

private val loadGoalsListModule by lazy { loadKoinModules(goalsListModule) }

internal fun injectGoalsListDependencies() = loadGoalsListModule