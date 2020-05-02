package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.goalslists.domain.usecase.GetAllDoneGoals
import oliveira.fabio.challenge52.goalslists.domain.usecase.GetAllOpenedGoalsUseCase
import oliveira.fabio.challenge52.goalslists.domain.usecase.impl.GetAllDoneGoalsImpl
import oliveira.fabio.challenge52.goalslists.domain.usecase.impl.GetAllOpenedGoalsUseCaseImpl
import oliveira.fabio.challenge52.goalslists.presentation.viewmodel.GoalsListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalsListsModule {
    private val presentationModule = module {
        factory<GetAllOpenedGoalsUseCase> {
            GetAllOpenedGoalsUseCaseImpl(
                get()
            )
        }
        factory<GetAllDoneGoals> {
            GetAllDoneGoalsImpl(
                get()
            )
        }
        viewModel {
            GoalsListsViewModel(
                get(),
                get()
            )
        }
    }

    fun load() = loadKoinModules(listOf(presentationModule))
}