package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.domain.usecase.GoalCreateUseCase
import oliveira.fabio.challenge52.domain.usecase.impl.GoalCreateUseCaseImpl
import oliveira.fabio.challenge52.presentation.ui.activity.GoalCreateActivity
import oliveira.fabio.challenge52.presentation.viewmodel.GoalCreateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named
import org.koin.dsl.module

object GoalCreateModule {
    private val domainModule = module {
        scope(named<GoalCreateActivity>()) {
            scoped<GoalCreateUseCase> {
                GoalCreateUseCaseImpl(
                    get(),
                    get()
                )
            }
        }
    }

    private val presentationModule = module {
        scope(named<GoalCreateActivity>()) {
            viewModel { GoalCreateViewModel(get()) }
        }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}