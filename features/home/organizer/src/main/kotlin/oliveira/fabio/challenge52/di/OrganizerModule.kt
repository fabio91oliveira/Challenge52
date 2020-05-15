package oliveira.fabio.challenge52.di

import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeHideOptionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateBalanceUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GetBalanceByDateAndTypeUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GetTransactionsByFilterUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToNextDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToPreviousDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.RemoveTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.ResetDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.SortTransactionsUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.UpdateBalanceAfterCreateTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.UpdateBalanceAfterRemoveTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.ChangeHideOptionUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.CreateBalanceUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.CreateTransactionUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.GetBalanceByDateAndTypeUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.GetTransactionsByFilterUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.GoToNextDateUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.GoToPreviousDateUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.RemoveTransactionUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.ResetDateUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.SortTransactionsUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.UpdateBalanceAfterCreateTransactionUseCaseImpl
import oliveira.fabio.challenge52.organizer.domain.usecase.impl.UpdateBalanceAfterRemoveTransactionUseCaseImpl
import oliveira.fabio.challenge52.organizer.presentation.viewmodel.OrganizerViewModel
import oliveira.fabio.challenge52.presentation.vo.Balance
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.util.*

object OrganizerModule {
    private val domainModule = module {
        factory<SortTransactionsUseCase> { SortTransactionsUseCaseImpl() }
        factory<GetBalanceByDateAndTypeUseCase> { GetBalanceByDateAndTypeUseCaseImpl(get(), get()) }
        factory<GetTransactionsByFilterUseCase> { GetTransactionsByFilterUseCaseImpl(get()) }
        factory<GoToNextDateUseCase> { GoToNextDateUseCaseImpl() }
        factory<GoToPreviousDateUseCase> { GoToPreviousDateUseCaseImpl() }
        factory<ResetDateUseCase> { ResetDateUseCaseImpl() }
        factory<CreateBalanceUseCase> { CreateBalanceUseCaseImpl(get(), get()) }
        factory<CreateTransactionUseCase> { CreateTransactionUseCaseImpl(get()) }
        factory<ChangeHideOptionUseCase> { ChangeHideOptionUseCaseImpl(get()) }
        factory<RemoveTransactionUseCase> { RemoveTransactionUseCaseImpl(get()) }
        factory<UpdateBalanceAfterRemoveTransactionUseCase> { UpdateBalanceAfterRemoveTransactionUseCaseImpl() }
        factory<UpdateBalanceAfterCreateTransactionUseCase> { UpdateBalanceAfterCreateTransactionUseCaseImpl() }
    }
    private val presentationModule = module {
        viewModel {
            OrganizerViewModel(
                currentDate = Calendar.getInstance(),
                balance = Balance(),
                getBalanceByDateAndTypeUseCase = get(),
                getTransactionsByFilterUseCase = get(),
                goToNextDateUseCase = get(),
                goToPreviousDateUseCase = get(),
                resetDateUseCase = get(),
                createBalanceUseCase = get(),
                createTransactionUseCase = get(),
                changeHideOptionUseCase = get(),
                removeTransactionUseCase = get(),
                updateBalanceAfterRemoveTransactionUseCase = get(),
                updateBalanceAfterCreateTransactionUseCase = get()
            )
        }
    }

    fun load() = loadKoinModules(listOf(domainModule, presentationModule))
}