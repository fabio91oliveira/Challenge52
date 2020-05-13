package oliveira.fabio.challenge52.organizer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.map
import features.home.organizer.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.extensions.getDateStringByFormat
import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeHideOptionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateBalanceUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GetBalanceByDateAndTypeUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GetTransactionsByFilter
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToNextDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GoToPreviousDateUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.RemoveTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.ResetDateUseCase
import oliveira.fabio.challenge52.organizer.presentation.action.OrganizerActions
import oliveira.fabio.challenge52.organizer.presentation.viewstate.Dialog
import oliveira.fabio.challenge52.organizer.presentation.viewstate.OrganizerViewState
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
    private val getTransactionsByFilter: GetTransactionsByFilter,
    private val goToNextDateUseCase: GoToNextDateUseCase,
    private val goToPreviousDateUseCase: GoToPreviousDateUseCase,
    private val changeHideOptionUseCase: ChangeHideOptionUseCase,
    private val resetDateUseCase: ResetDateUseCase,
    private val createBalanceUseCase: CreateBalanceUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val removeTransactionUseCase: RemoveTransactionUseCase
) : ViewModel() {

    private val _organizerActions by lazy { MutableLiveData<OrganizerActions>() }
    val organizerActions: LiveData<OrganizerActions> = _organizerActions

    private val _organizerViewState by lazy { MutableLiveData<OrganizerViewState>() }
    val organizerViewState: LiveData<OrganizerViewState> = _organizerViewState

    init {
        initState(getFormattedDate())
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
                        delay(500)
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
                ).fold(
                    success = {
                        getBalance()
                    },
                    failure = {
                        Timber.e(it)
                    }
                )
            } ?: run {
                Result.of(createBalanceUseCase(balance))
                    .map {
                        createTransactionUseCase(transaction, it)
                    }
            }.fold(
                success = {
                    getBalance()
                },
                failure = {
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
                    isEmptyStateFilterTransactionVisible = false,
                    isChipsEnabled = false
                )
            }
            Result.of(
                getTransactionsByFilter(
                    balance,
                    filter
                )
            ).fold(
                success = {
                    OrganizerActions.UpdateTransactions(balance.transactionsFiltered).sendAction()
                    setViewState { viewState ->
                        viewState.copy(
                            isLoadingTransactions = false,
                            isTransactionsVisible = balance.transactionsFiltered.isNotEmpty(),
                            isEmptyStateFilterTransactionVisible = balance.transactionsFiltered.isEmpty(),
                            isChipsEnabled = true
                        )
                    }
                },
                failure = {
                    setViewState { viewState ->
                        viewState.copy(
                            isLoadingTransactions = false,
                            isTransactionsVisible = false,
                            isEmptyStateFilterTransactionVisible = true,
                            isChipsEnabled = true
                        )
                    }
                }
            )
        }
    }

    fun removeTransaction(transaction: Transaction) {
        viewModelScope.launch {
            setViewState {
                it.copy(
                    dialog = Dialog.NoDialog,
                    isLoadingRemove = true
                )
            }
            Result.of(removeTransactionUseCase(transaction))
                .fold(
                    success = {
                        getBalance()
                    },
                    failure = {
                        //todo fazer nada no adapter
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

    fun hideDialogs() {
        setViewState {
            it.copy(dialog = Dialog.NoDialog)
        }
    }

    fun showConfirmationDialogRemoveTransaction(position: Int) =
        setViewState {
            it.copy(
                dialog = Dialog.ConfirmationDialogRemoveTransaction(
                    R.drawable.ic_business_man,
                    R.string.organizer_dialog_remove_transaction_title,
                    R.string.organizer_dialog_remove_transaction_description,
                    position
                )
            )
        }

    private fun getBalance() {
        viewModelScope.launch {
            resetTransactionFilter()
            setViewState { viewState ->
                viewState.copy(
                    isLoadingBalance = true,
                    isLoadingTransactions = true,
                    isTransactionsVisible = false,
                    isChipsEnabled = false,
                    isEmptyStateFilterTransactionVisible = false,
                    isEmptyStateVisible = false
                )
            }
            Result.of(getBalanceByDateAndTypeUseCase(currentDate))
                .map {
                    it.apply {
                        balance = this
                    }
                }
                .fold(
                    success = {
                        OrganizerActions.ShowBalance(it).sendAction()
                        setViewState { viewState ->
                            viewState.copy(
                                isLoadingBalance = false,
                                isLoadingTransactions = false,
                                isHide = it.isHide,
                                isTransactionsVisible = it.transactionsFiltered.isNotEmpty(),
                                isEmptyStateVisible = it.transactionsFiltered.isEmpty()
                            )
                        }
                    },
                    failure = {
                        resetDate()
                        Timber.e(it)
                    }
                )
        }
    }

    private fun resetTransactionFilter() {
        OrganizerActions.ResetTransactionsFilter.sendAction()
    }

    private fun resetDate() {
        viewModelScope.launch {
            Result.of(resetDateUseCase(currentDate))
                .fold(
                    success = {
                        setViewState {
                            it.copy(currentMonthYear = getFormattedDate())
                        }
                    },
                    failure = {
                        Timber.e(it)
                    }
                )
        }
    }

    private fun getFormattedDate() = currentDate.time.getDateStringByFormat(DATE_PATTERN)

    private fun OrganizerActions.sendAction() {
        _organizerActions.value = this
    }

    private fun initState(currentMonthYear: String) {
        _organizerViewState.value = OrganizerViewState.init(currentMonthYear)
    }

    private fun setViewState(state: (OrganizerViewState) -> OrganizerViewState) {
        _organizerViewState.value?.also {
            _organizerViewState.value = state(it)
        }
    }

    companion object {
        private const val DATE_PATTERN = "MMMM/yyyy"
    }
}