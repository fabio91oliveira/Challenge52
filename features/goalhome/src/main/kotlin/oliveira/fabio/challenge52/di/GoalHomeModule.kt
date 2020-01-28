package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel.GoalsListsViewModel
import oliveira.fabio.challenge52.home.help.presentation.viewmodel.HelpViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalHomeModule {
    private val presentationModule = module {
        viewModel {
            GoalsListsViewModel(
                get(),
                get(),
                get()
            )
        }
        viewModel { HelpViewModel() }
    }

    fun load() = loadKoinModules(listOf(presentationModule))
}