package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.CreateScreensUseCase
import oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.impl.CreateScreensUseCaseImpl
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.viewmodel.ChallengeOverviewViewModel
import oliveira.fabio.challenge52.challenge.challengeselect.domain.usecase.GetChallengesUseCase
import oliveira.fabio.challenge52.challenge.challengeselect.domain.usecase.impl.GetChallengesUseCaseImpl
import oliveira.fabio.challenge52.challenge.challengeselect.presentation.viewmodel.ChallengeSelectViewModel
import oliveira.fabio.challenge52.domain.vo.Challenge
import oliveira.fabio.challenge52.goalcreate.domain.mapper.WeekMapper
import oliveira.fabio.challenge52.goalcreate.domain.mapper.impl.WeekMapperImpl
import oliveira.fabio.challenge52.goalcreate.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.goalcreate.domain.usecase.GetGoalSuggestionsUseCase
import oliveira.fabio.challenge52.goalcreate.domain.usecase.impl.AddGoalUseCaseImpl
import oliveira.fabio.challenge52.goalcreate.domain.usecase.impl.GetGoalSuggestionsUseCaseImpl
import oliveira.fabio.challenge52.goalcreate.presentation.viewmodel.GoalCreateChooseNameViewModel
import oliveira.fabio.challenge52.goalcreate.presentation.viewmodel.GoalCreateNameSuggestionsViewModel
import oliveira.fabio.challenge52.goalcreate.presentation.viewmodel.GoalCreateViewModel_old
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalCreateModule {
    private val domainModule = module {
        factory<WeekMapper> { WeekMapperImpl() }
        factory<AddGoalUseCase> {
            AddGoalUseCaseImpl(
                get(),
                get(),
                get()
            )
        }
        factory<CreateScreensUseCase> { CreateScreensUseCaseImpl() }
        factory<GetChallengesUseCase> { GetChallengesUseCaseImpl() }
        factory<GetGoalSuggestionsUseCase> { GetGoalSuggestionsUseCaseImpl() }
    }

    private val presentationModule = module {
        viewModel {
            GoalCreateViewModel_old(
                get()
            )
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
            GoalCreateNameSuggestionsViewModel(get())
        }
        viewModel {
            GoalCreateChooseNameViewModel()
        }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}