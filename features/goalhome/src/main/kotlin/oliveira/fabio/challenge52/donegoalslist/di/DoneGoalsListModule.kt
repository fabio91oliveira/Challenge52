package oliveira.fabio.challenge52.donegoalslist.di

import oliveira.fabio.challenge52.donegoalslist.viewmodel.DoneGoalsListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

private val doneGoalsListModule = module {
    viewModel { DoneGoalsListViewModel(get(), get(), get()) }
}

private val loadDoneGoalsListModule by lazy { loadKoinModules(doneGoalsListModule) }

internal fun injectDoneGoalsListDependencies() = loadDoneGoalsListModule