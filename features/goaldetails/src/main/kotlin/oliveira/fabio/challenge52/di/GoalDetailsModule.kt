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
import oliveira.fabio.challenge52.presentation.ui.activity.GoalDetailsActivity
import oliveira.fabio.challenge52.presentation.viewmodel.GoalDetailsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GoalDetailsModule {
    private val domainModule = module {
        scope(named<GoalDetailsActivity>()) {
            scoped<ItemMapper> {
                ItemsMapperImpl()
            }
            scoped<GetItemsListUseCase> {
                GetItemsListUseCaseImpl(get())
            }
            scoped<ChangeWeekDepositStatusUseCase> {
                ChangeWeekDepositStatusUseCaseImpl(get())
            }
            scoped<SetGoalAsDoneUseCase> {
                SetGoalAsDoneUseCaseImpl(
                    get(),
                    get()
                )
            }
            scoped<RemoveGoalUseCase> {
                RemoveGoalUseCaseImpl(
                    get(),
                    get()
                )
            }
            scoped<CheckAllWeeksAreDepositedUseCase> {
                CheckAllWeeksAreDepositedUseCaseImpl()
            }
        }
    }

    private val presentationModule = module {
        scope(named<GoalDetailsActivity>()) {
            viewModel { GoalDetailsViewModel(get(), get(), get(), get(), get()) }
        }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}