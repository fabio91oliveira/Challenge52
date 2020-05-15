package oliveira.fabio.challenge52.organizer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import features.home.organizer.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.event.SingleLiveEvent
import oliveira.fabio.challenge52.extensions.getDateStringByFormat
import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeHideOptionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateBalanceUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GetBalanceByDateAndTypeUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GetTransactionsByFilterUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToNextDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToPreviousDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.RemoveTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.ResetDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.UpdateBalanceAfterCreateTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.UpdateBalanceAfterRemoveTransactionUseCase
import oliveira.fabio.challenge52.organizer.presentation.action.OrganizerActions
import oliveira.fabio.challenge52.organizer.presentation.viewstate.OrganizerBalanceViewState
import oliveira.fabio.challenge52.organizer.presentation.viewstate.OrganizerTransactionsViewState
import oliveira.fabio.challenge52.organizer.presentation.viewstate.OrganizerViewState
import oliveira.fabio.challenge52.organizer.presentation.vo.BalanceBottom
import oliveira.fabio.challenge52.organizer.presentation.vo.BalanceTop
import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum
import timber.log.Timber
import java.util.*
import kotlin.random.Random

internal class OrganizerViewModel(
    private val currentDate: Calendar,
    private var balance: Balance,
    private val getBalanceByDateAndTypeUseCase: GetBalanceByDateAndTypeUseCase,
    private val getTransactionsByFilterUseCase: GetTransactionsByFilterUseCase,
    private val goToNextDateUseCase: GoToNextDateUseCase,
    private val goToPreviousDateUseCase: GoToPreviousDateUseCase,
    private val changeHideOptionUseCase: ChangeHideOptionUseCase,
    private val resetDateUseCase: ResetDateUseCase,
    private val createBalanceUseCase: CreateBalanceUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val removeTransactionUseCase: RemoveTransactionUseCase,
    private val updateBalanceAfterRemoveTransactionUseCase: UpdateBalanceAfterRemoveTransactionUseCase,
    private val updateBalanceAfterCreateTransactionUseCase: UpdateBalanceAfterCreateTransactionUseCase
) : ViewModel() {

    private val _organizerActions by lazy { SingleLiveEvent<OrganizerActions>() }
    val organizerActions: LiveData<OrganizerActions> = _organizerActions

    private val _organizerViewState by lazy { MutableLiveData<OrganizerViewState>() }
    val organizerViewState: LiveData<OrganizerViewState> = _organizerViewState

    private val _organizerBalanceViewState by lazy { MutableLiveData<OrganizerBalanceViewState>() }
    val organizerBalanceViewState: LiveData<OrganizerBalanceViewState> = _organizerBalanceViewState

    private val _organizerTransactionsViewState by lazy { MutableLiveData<OrganizerTransactionsViewState>() }
    val organizerTransactionsViewState: LiveData<OrganizerTransactionsViewState> =
        _organizerTransactionsViewState

    init {
        initStates(getFormattedDate())
        getBalance()
    }

    fun goToPreviousMonth() {
        viewModelScope.launch {
            Result.of(goToPreviousDateUseCase(currentDate))
                .fold(
                    success = {
                        setViewState {
                            it.copy(currentMonthYear = getFormattedDate())
                        }
                        getBalance()
                    },
                    failure = {
                        Timber.e(it)
                    }
                )
        }
    }

    fun goToNextMonth() {
        viewModelScope.launch {
            Result.of(goToNextDateUseCase(currentDate))
                .fold(
                    success = {
                        setViewState {
                            it.copy(
                                currentMonthYear = getFormattedDate()
                            )
                        }
                        getBalance()
                    },
                    failure = {
                        Timber.e(it)
                    }
                )
        }
    }

    fun addTransaction() {
        viewModelScope.launch {
            val number = Random.nextInt(0, 10)
            val transaction = Transaction(
                icoResource = if (number > 5) "ic_salary" else "ic_regular",
                description = "Transacao Exemplo $number",
                date = currentDate.time,
                money = 200.0 * number.toDouble(),
                typeTransaction = if (number > 5) TypeTransactionEnum.INCOME else TypeTransactionEnum.SPENT
            )

            balance.id?.also { idBalance ->
                Result.of(
                    createTransactionUseCase(transaction, idBalance)
                ).map {
                    transaction.id = it
                    updateBalanceAfterCreateTransactionUseCase(balance, transaction)
                }.fold(
                    success = {
                        getBalance(false)
                        OrganizerActions.ShowConfirmationMessage(R.string.organizer_dialog_add_transaction_message_confirmation)
                            .sendAction()
                    },
                    failure = {
                        OrganizerActions.ShowConfirmationMessage(R.string.organizer_dialog_update_transaction_message_confirmation_error)
                            .sendAction()
                        Timber.e(it)
                    }
                )
            } ?: run {
                Result.of(createBalanceUseCase(balance))
                    .map {
                        createTransactionUseCase(transaction, it)
                    }
                    .map {
                        transaction.id = it
                        updateBalanceAfterCreateTransactionUseCase(balance, transaction)
                    }
            }.fold(
                success = {
                    getBalance(false)
                    OrganizerActions.ShowConfirmationMessage(R.string.organizer_dialog_add_transaction_message_confirmation)
                        .sendAction()
                },
                failure = {
                    OrganizerActions.ShowConfirmationMessage(R.string.organizer_dialog_update_transaction_message_confirmation_error)
                        .sendAction()
                    getBalance()
                    Timber.e(it)
                }
            )
        }
    }

    fun removeTransaction(
        transaction: Transaction,
        position: Int
    ) {
        viewModelScope.launch {
            setViewState {
                it.copy(
                    isLoadingRemove = true
                )
            }
            Result.of(removeTransactionUseCase(transaction))
                .map {
                    updateBalanceAfterRemoveTransactionUseCase(balance, transaction)
                }
                .fold(
                    success = {
                        setViewState { viewState ->
                            viewState.copy(
                                isTransactionsVisible = balance.transactionsFiltered.isNotEmpty(),
                                isEmptyStateVisible = balance.transactionsFiltered.isEmpty(),
                                isFiltersVisible = balance.allTransactions.isNotEmpty() || balance.transactionsFiltered.isNotEmpty(),
                                isLoadingRemove = false
                            )
                        }
                        setBalanceValues(balance)
                        setTransactionsValues(balance)
                        OrganizerActions.UpdateTransactionsAfterRemove(position).sendAction()
                        OrganizerActions.ShowConfirmationMessage(R.string.organizer_dialog_remove_transaction_message_confirmation)
                            .sendAction()
                    },
                    failure = {
                        setViewState { viewState ->
                            viewState.copy(
                                isLoadingRemove = false
                            )
                        }
                        OrganizerActions.UpdateTransactions.sendAction()
                        OrganizerActions.ShowConfirmationMessage(R.string.organizer_dialog_update_transaction_message_confirmation_error)
                            .sendAction()
                        getBalance()
                        Timber.e(it)
                    }
                )
        }
    }

    fun changeHideOption() {
        viewModelScope.launch {
            balance.id?.also { balanceId ->
                setViewState { viewState ->
                    viewState.copy(isHideLoading = true)
                }

                Result.of(changeHideOptionUseCase(balanceId, balance.isHide.not()))
                    .map {
                        balance.isHide = balance.isHide.not()
                    }
                    .fold(
                        success = {
                            setViewState {
                                it.copy(
                                    isHide = balance.isHide,
                                    isHideLoading = false
                                )
                            }
                        },
                        failure = {
                            setViewState { viewState ->
                                viewState.copy(isHideLoading = false)
                            }
                            Timber.e(it)
                        }
                    )
            }
        }
    }

    fun changeTransactionFilter(filter: String) {
        viewModelScope.launch {
            setViewState {
                it.copy(
                    isLoadingTransactions = true,
                    isTransactionsVisible = false,
                    isEmptyStateVisible = false,
                    isLoadingFilters = true,
                    isFiltersVisible = false,
                    isEmptyStateFilterTransactionVisible = false
                )
            }
            Result.of(
                getTransactionsByFilterUseCase(
                    balance,
                    filter
                )
            ).fold(
                success = {
                    setViewState { viewState ->
                        viewState.copy(
                            isLoadingTransactions = false,
                            isTransactionsVisible = balance.transactionsFiltered.isNotEmpty(),
                            isEmptyStateFilterTransactionVisible = balance.transactionsFiltered.isEmpty(),
                            isLoadingFilters = false,
                            isFiltersVisible = true
                        )
                    }
                    setTransactionsValues(balance)
                    OrganizerActions.UpdateTransactions.sendAction()
                },
                failure = {
                    setViewState { viewState ->
                        viewState.copy(
                            isLoadingTransactions = false,
                            isTransactionsVisible = false,
                            isEmptyStateFilterTransactionVisible = true
                        )
                    }
                }
            )
        }
    }

    fun getBalance(hasToShowLoadingBalance: Boolean = true) {
        viewModelScope.launch {
            resetFilters()
            setViewState { viewState ->
                viewState.copy(
                    isLoadingBalance = hasToShowLoadingBalance,
                    isLoadingTransactions = true,
                    isTransactionsVisible = false,
                    isEmptyStateFilterTransactionVisible = false,
                    isEmptyStateVisible = false,
                    isLoadingFilters = true,
                    isFiltersVisible = false,
                    isChangeDateEnabled = false
                )
            }
            Result.of(getBalanceByDateAndTypeUseCase(currentDate))
                .fold(
                    success = {
                        setViewState { viewState ->
                            viewState.copy(
                                isLoadingBalance = false,
                                isLoadingTransactions = false,
                                isHide = it.isHide,
                                isTransactionsVisible = it.transactionsFiltered.isNotEmpty(),
                                isEmptyStateVisible = it.transactionsFiltered.isEmpty(),
                                isLoadingFilters = false,
                                isFiltersVisible = it.transactionsFiltered.isNotEmpty(),
                                isChangeDateEnabled = true
                            )
                        }
                        balance = it
                        setBalanceValues(it)
                        setTransactionsValues(it)
                        OrganizerActions.UpdateTransactions.sendAction()
                    },
                    failure = {
                        resetDate()
                        Timber.e(it)
                    }
                )
        }
    }

    private fun resetDate() {
        viewModelScope.launch {
            Result.of(resetDateUseCase(currentDate))
                .fold(
                    success = {
                        setViewState {
                            it.copy(
                                currentMonthYear = getFormattedDate(),
                                isChangeDateEnabled = true
                            )
                        }
                    },
                    failure = {
                        Timber.e(it)
                    }
                )
        }
    }

    fun showAddButton() {
        setViewState {
            it.copy(isAddButtonVisible = true)
        }
    }

    fun hideAddButton() {
        setViewState {
            it.copy(isAddButtonVisible = false)
        }
    }

    private fun getFormattedDate() = currentDate.time.getDateStringByFormat(DATE_PATTERN)

    private fun resetFilters() {
        OrganizerActions.ResetFilters.sendAction()
    }

    private fun setBalanceValues(balance: Balance) {
        setBalanceViewState { balanceViewState ->
            balanceViewState.copy(
                balanceTop = BalanceTop(
                    total = balance.total,
                    totalIncomes = balance.totalIncomes,
                    totalSpent = balance.totalSpent,
                    currentLocale = balance.currentLocale
                )
            )
        }
    }

    private fun setTransactionsValues(balance: Balance) {
        setTransactionsViewState { transactionsViewState ->
            transactionsViewState.copy(
                balanceBottom = BalanceBottom(
                    totalAllFilter = balance.totalAllFilter,
                    totalIncomeFilter = balance.totalIncomeFilter,
                    totalSpentFilter = balance.totalSpentFilter,
                    transactions = balance.transactionsFiltered,
                    currentLocale = balance.currentLocale
                )
            )
        }
    }

    private fun OrganizerActions.sendAction() {
        _organizerActions.value = this
    }

    private fun initStates(currentMonthYear: String) {
        _organizerViewState.value = OrganizerViewState.init(currentMonthYear)
        _organizerBalanceViewState.value = OrganizerBalanceViewState.init()
        _organizerTransactionsViewState.value = OrganizerTransactionsViewState.init()
    }

    private fun setViewState(state: (OrganizerViewState) -> OrganizerViewState) {
        _organizerViewState.value?.also {
            _organizerViewState.value = state(it)
        }
    }

    private fun setBalanceViewState(state: (OrganizerBalanceViewState) -> OrganizerBalanceViewState) {
        _organizerBalanceViewState.value?.also {
            _organizerBalanceViewState.value = state(it)
        }
    }

    private fun setTransactionsViewState(state: (OrganizerTransactionsViewState) -> OrganizerTransactionsViewState) {
        _organizerTransactionsViewState.value?.also {
            _organizerTransactionsViewState.value = state(it)
        }
    }

    companion object {
        private const val DATE_PATTERN = "MMMM/yyyy"
    }
}