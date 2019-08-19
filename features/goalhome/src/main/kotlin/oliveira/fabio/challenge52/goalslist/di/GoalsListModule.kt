package oliveira.fabio.challenge52.goalslist.di

import oliveira.fabio.challenge52.goalslist.viewmodel.GoalsListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private val goalsListModule = module {
    viewModel { GoalsListViewModel(get(), get(), get()) }
}

private val loadGoalsListModule by lazy { loadKoinModules(goalsListModule) }

internal fun injectGoalsListDependencies() = loadGoalsListModule