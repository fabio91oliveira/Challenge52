package oliveira.fabio.challenge52.di

import androidx.lifecycle.SavedStateHandle
import oliveira.fabio.challenge52.domain.mapper.DetailsMapper
import oliveira.fabio.challenge52.domain.mapper.impl.DetailsMapperImpl
import oliveira.fabio.challenge52.domain.usecase.ChangeItemStatusUseCase
import oliveira.fabio.challenge52.domain.usecase.MountGoalsDetailsUseCase
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase
import oliveira.fabio.challenge52.domain.usecase.VerifyAllWeekAreCompletedUseCase
import oliveira.fabio.challenge52.domain.usecase.impl.ChangeItemStatusUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.MountGoalsDetailsUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.RemoveGoalUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.SetGoalAsDoneUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.VerifyAllWeekAreCompletedUseCaseImpl
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalDetailsModule {
    private val domainModule = module {
        factory<DetailsMapper> {
            DetailsMapperImpl()
        }
        factory<MountGoalsDetailsUseCase> {
            MountGoalsDetailsUseCaseImpl(get())
        }
        factory<ChangeItemStatusUseCase> {
            ChangeItemStatusUseCaseImpl(get())
        }
        factory<SetGoalAsDoneUseCase> {
            SetGoalAsDoneUseCaseImpl(
                get()
            )
        }
        factory<RemoveGoalUseCase> {
            RemoveGoalUseCaseImpl(
                get(),
                get()
            )
        }
        factory<VerifyAllWeekAreCompletedUseCase> {
            VerifyAllWeekAreCompletedUseCaseImpl()
        }
    }

    private val presentationModule = module {
        viewModel { (handle: SavedStateHandle) ->
            GoalDetailsViewModel(handle, get(), get(), get(), get(), get())
        }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}