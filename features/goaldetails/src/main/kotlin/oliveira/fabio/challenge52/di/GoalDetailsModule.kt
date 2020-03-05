package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.mapper.WeekDetailsMapper
import oliveira.fabio.challenge52.domain.mapper.impl.WeekDetailsMapperImpl
import oliveira.fabio.challenge52.domain.usecase.ChangeWeekStatusUseCase
import oliveira.fabio.challenge52.domain.usecase.VerifyAllWeekAreCompletedUseCase
import oliveira.fabio.challenge52.domain.usecase.MountWeeksListUseCase
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase
import oliveira.fabio.challenge52.domain.usecase.impl.ChangeWeekStatusUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.VerifyAllWeekAreCompletedUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.MountWeeksListUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.RemoveGoalUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.SetGoalAsDoneUseCaseImpl
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalDetailsModule {
    private val domainModule = module {
        factory<WeekDetailsMapper> {
            WeekDetailsMapperImpl()
        }
        factory<MountWeeksListUseCase> {
            MountWeeksListUseCaseImpl(get())
        }
        factory<ChangeWeekStatusUseCase> {
            ChangeWeekStatusUseCaseImpl(get())
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
        viewModel { GoalDetailsViewModel(get(), get(), get(), get(), get()) }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}