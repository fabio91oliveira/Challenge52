package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.mapper.WeekMapper
import oliveira.fabio.challenge52.domain.mapper.impl.WeekMapperImpl
import oliveira.fabio.challenge52.domain.usecase.AddGoalUseCase
import oliveira.fabio.challenge52.domain.usecase.impl.AddGoalUseCaseImpl
import oliveira.fabio.challenge52.presentation.viewmodel.GoalCreateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object GoalCreateModule {
    private val domainModule = module {
        factory<WeekMapper> { WeekMapperImpl() }
        factory<AddGoalUseCase> {
            AddGoalUseCaseImpl(get(), get(), get())
        }
    }

    private val presentationModule = module {
        viewModel { GoalCreateViewModel(get()) }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}