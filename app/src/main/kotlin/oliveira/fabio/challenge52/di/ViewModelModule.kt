package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.feature.donegoalslist.viewmodel.DoneGoalsListViewModel
import oliveira.fabio.challenge52.feature.goalcreate.viewmodel.GoalCreateViewModel
import oliveira.fabio.challenge52.feature.goaldetails.viewmodel.GoalDetailsViewModel
import oliveira.fabio.challenge52.feature.goalslist.viewmodel.GoalsListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { GoalCreateViewModel(get()) }
    viewModel { GoalsListViewModel(get()) }
    viewModel { DoneGoalsListViewModel(get()) }
    viewModel { GoalDetailsViewModel(get()) }
}