package oliveira.fabio.challenge52.donegoalslist.di

import oliveira.fabio.challenge52.donegoalslist.presentation.viewmodel.DoneGoalsListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val doneGoalsListModule = module {
    viewModel { DoneGoalsListViewModel(get(), get(), get()) }
}

private val loadDoneGoalsListModule by lazy { loadKoinModules(doneGoalsListModule) }

internal fun injectDoneGoalsListDependencies() = loadDoneGoalsListModule