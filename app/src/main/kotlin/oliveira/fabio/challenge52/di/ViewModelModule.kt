package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.feature.goalcreate.viewmodel.GoalCreateViewModel
import oliveira.fabio.challenge52.feature.goallist.viewmodel.GoalsListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { GoalCreateViewModel(get()) }
    viewModel { GoalsListViewModel(get()) }
}