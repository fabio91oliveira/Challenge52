package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.mapper.ItemMapper
import oliveira.fabio.challenge52.domain.mapper.impl.ItemsMapperImpl
import oliveira.fabio.challenge52.domain.usecase.ChangeWeekDepositStatusUseCase
import oliveira.fabio.challenge52.domain.usecase.CheckAllWeeksAreDepositedUseCase
import oliveira.fabio.challenge52.domain.usecase.GetItemsListUseCase
import oliveira.fabio.challenge52.domain.usecase.RemoveGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.SetGoalAsDoneUseCase
import oliveira.fabio.challenge52.domain.usecase.impl.ChangeWeekDepositStatusUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.CheckAllWeeksAreDepositedUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.GetItemsListUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.RemoveGoalUseCaseImpl
import oliveira.fabio.challenge52.domain.usecase.impl.SetGoalAsDoneUseCaseImpl
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalDetailsModule {
    private val domainModule = module {
        factory<ItemMapper> {
            ItemsMapperImpl()
        }
        factory<GetItemsListUseCase> {
            GetItemsListUseCaseImpl(get())
        }
        factory<ChangeWeekDepositStatusUseCase> {
            ChangeWeekDepositStatusUseCaseImpl(get())
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
        factory<CheckAllWeeksAreDepositedUseCase> {
            CheckAllWeeksAreDepositedUseCaseImpl()
        }
    }

    private val presentationModule = module {
        viewModel { GoalDetailsViewModel(get(), get(), get(), get(), get()) }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}