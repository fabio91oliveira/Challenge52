package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.principal.donegoalslist.presentation.viewmodel.DoneGoalsListViewModel
import oliveira.fabio.challenge52.principal.goalslist.presentation.viewmodel.GoalsListViewModel
import oliveira.fabio.challenge52.principal.help.presentation.viewmodel.HelpViewModel
import oliveira.fabio.challenge52.principal.home.presentation.ui.activity.HomeActivity
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GoalHomeModule {
    private val presentationModule = module {
        scope(named<HomeActivity>()) {
            viewModel { HelpViewModel() }
            viewModel {
                GoalsListViewModel(
                    get(),
                    get(),
                    get()
                )
            }
            viewModel {
                DoneGoalsListViewModel(
                    get(),
                    get(),
                    get()
                )
            }
        }
    }

    fun load() = loadKoinModules(listOf(presentationModule))
}