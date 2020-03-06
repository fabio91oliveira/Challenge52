package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.home.goalslists.domain.usecase.GetAllDoneGoals
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.GetAllOpenedGoals
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.impl.GetAllDoneGoalsImpl
import oliveira.fabio.challenge52.home.goalslists.domain.usecase.impl.GetAllOpenedGoalsImpl
import oliveira.fabio.challenge52.home.goalslists.presentation.viewmodel.GoalsListsViewModel
import oliveira.fabio.challenge52.home.help.presentation.viewmodel.HelpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalHomeModule {
    private val presentationModule = module {
        factory<GetAllOpenedGoals> { GetAllOpenedGoalsImpl(get()) }
        factory<GetAllDoneGoals> { GetAllDoneGoalsImpl(get()) }
        viewModel {
            GoalsListsViewModel(
                get(),
                get()
            )
        }
        viewModel { HelpViewModel() }
    }

    fun load() = loadKoinModules(listOf(presentationModule))
}