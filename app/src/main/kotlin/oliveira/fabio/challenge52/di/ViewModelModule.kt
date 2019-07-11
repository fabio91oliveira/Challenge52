package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.donegoalslist.viewmodel.DoneGoalsListViewModel
import oliveira.fabio.challenge52.goalslist.viewmodel.GoalsListViewModel
import oliveira.fabio.challenge52.help.viewmodel.HelpViewModel
import oliveira.fabio.challenge52.presentation.viewmodel.GoalCreateViewModel
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { GoalCreateViewModel(get()) }
    viewModel { GoalsListViewModel(get(), get(), get()) }
    viewModel { DoneGoalsListViewModel(get(), get(), get()) }
    viewModel { GoalDetailsViewModel(get()) }
    viewModel { HelpViewModel() }
}