package oliveira.fabio.challenge52.di

import androidx.lifecycle.SavedStateHandle
import oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.CreateScreensUseCase
import oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.impl.CreateScreensUseCaseImpl
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.viewmodel.ChallengeOverviewViewModel
import oliveira.fabio.challenge52.challenge.challengeselect.domain.usecase.GetChallengesUseCase
import oliveira.fabio.challenge52.challenge.challengeselect.domain.usecase.impl.GetChallengesUseCaseImpl
import oliveira.fabio.challenge52.challenge.challengeselect.presentation.viewmodel.ChallengeSelectViewModel
import oliveira.fabio.challenge52.creategoal.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.AddItemsUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.CalculateMoneyUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.CreateGoalToSaveObjectUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.CreateItemsUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.GetCurrentLocaleUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.GetGoalSuggestionsUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.GetMoneySuggestionsUseCase
import oliveira.fabio.challenge52.creategoal.domain.usecase.impl.AddGoalUseCaseImpl
import oliveira.fabio.challenge52.creategoal.domain.usecase.impl.AddItemsUseCaseImpl
import oliveira.fabio.challenge52.creategoal.domain.usecase.impl.CalculateMoneyUseCaseImpl
import oliveira.fabio.challenge52.creategoal.domain.usecase.impl.CreateGoalToSaveObjectUseCaseImpl
import oliveira.fabio.challenge52.creategoal.domain.usecase.impl.CreateItemsUseCaseImpl
import oliveira.fabio.challenge52.creategoal.domain.usecase.impl.GetCurrentLocaleUseCaseImpl
import oliveira.fabio.challenge52.creategoal.domain.usecase.impl.GetGoalSuggestionsUseCaseImpl
import oliveira.fabio.challenge52.creategoal.domain.usecase.impl.GetMoneySuggestionsUseCaseImpl
import oliveira.fabio.challenge52.creategoal.presentation.viewmodel.CreateGoalViewModel
import oliveira.fabio.challenge52.creategoal.presentation.viewmodel.GoalChooseNameViewModel
import oliveira.fabio.challenge52.creategoal.presentation.viewmodel.GoalSuggestionsListViewModel
import oliveira.fabio.challenge52.domain.vo.Challenge
import oliveira.fabio.challenge52.features.CreateGoalNavigation
import oliveira.fabio.challenge52.presentation.navigation.CreateGoalNavigationImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalCreateModule {
    private val domainModule = module {
        factory<CreateScreensUseCase> { CreateScreensUseCaseImpl() }
        factory<GetChallengesUseCase> { GetChallengesUseCaseImpl() }
        factory<GetGoalSuggestionsUseCase> { GetGoalSuggestionsUseCaseImpl() }
        factory<GetCurrentLocaleUseCase> { GetCurrentLocaleUseCaseImpl() }
        factory<CreateGoalToSaveObjectUseCase> { CreateGoalToSaveObjectUseCaseImpl(get()) }
        factory<GetMoneySuggestionsUseCase> { GetMoneySuggestionsUseCaseImpl(get()) }
        factory<AddGoalUseCase> { AddGoalUseCaseImpl(get()) }
        factory<AddItemsUseCase> { AddItemsUseCaseImpl(get()) }
        factory<CalculateMoneyUseCase> { CalculateMoneyUseCaseImpl() }
        factory<CreateItemsUseCase> { CreateItemsUseCaseImpl() }
    }

    private val presentationModule = module {
        factory<CreateGoalNavigation> { CreateGoalNavigationImpl() }
        viewModel {
            ChallengeSelectViewModel(
                get()
            )
        }
        viewModel { (challenge: Challenge) ->
            ChallengeOverviewViewModel(
                challenge,
                get()
            )
        }
        viewModel {
            GoalSuggestionsListViewModel(get(), get())
        }
        viewModel { (handle: SavedStateHandle) ->
            GoalChooseNameViewModel(handle)
        }
        viewModel { (handle: SavedStateHandle) ->
            CreateGoalViewModel(handle, get(), get(), get(), get(), get())
        }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}