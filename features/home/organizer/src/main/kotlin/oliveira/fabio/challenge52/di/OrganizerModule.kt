package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeHideOptionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeTransactionFilterUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateBalanceUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GetBalanceByDateAndTypeUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToNextDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToPreviousDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.RemoveTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.ResetDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.ChangeHideOptionUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.ChangeTransactionFilterUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.CreateBalanceUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.CreateTransactionUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.GetBalanceByDateAndTypeUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.GoToNextDateUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.GoToPreviousDateUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.RemoveTransactionUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.ResetDateUseCaseImpl
import oliveira.fabio.challenge52.organizer.presentation.viewmodel.OrganizerViewModel
import oliveira.fabio.challenge52.presentation.vo.Balance
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.util.*

object OrganizerModule {
    private val domainModule = module {
        factory<GetBalanceByDateAndTypeUseCase> { GetBalanceByDateAndTypeUseCaseImpl(get()) }
        factory<ChangeTransactionFilterUseCase> { ChangeTransactionFilterUseCaseImpl() }
        factory<GoToNextDateUseCase> { GoToNextDateUseCaseImpl() }
        factory<GoToPreviousDateUseCase> { GoToPreviousDateUseCaseImpl() }
        factory<ResetDateUseCase> { ResetDateUseCaseImpl() }
        factory<CreateBalanceUseCase> { CreateBalanceUseCaseImpl(get(), get()) }
        factory<CreateTransactionUseCase> { CreateTransactionUseCaseImpl(get()) }
        factory<ChangeHideOptionUseCase> { ChangeHideOptionUseCaseImpl(get()) }
        factory<RemoveTransactionUseCase> { RemoveTransactionUseCaseImpl(get()) }
    }
    private val presentationModule = module {
        viewModel { (calendar: Calendar, balance: Balance) ->
            OrganizerViewModel(
                currentDate = calendar,
                balance = balance,
                getBalanceByDateAndTypeUseCase = get(),
                changeTransactionFilterUseCase = get(),
                goToNextDateUseCase = get(),
                goToPreviousDateUseCase = get(),
                resetDateUseCase = get(),
                createBalanceUseCase = get(),
                createTransactionUseCase = get(),
                changeHideOptionUseCase = get(),
                removeTransactionUseCase = get()
            )
        }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}