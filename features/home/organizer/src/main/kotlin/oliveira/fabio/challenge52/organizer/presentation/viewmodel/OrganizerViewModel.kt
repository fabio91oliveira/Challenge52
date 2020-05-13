package oliveira.fabio.challenge52.organizer.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.home.organizer.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.extensions.getDateStringByFormat
import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeHideOptionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.ChangeTransactionFilterUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateBalanceUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.CreateTransactionUseCase
import oliveira.fabio.challenge52.organizer.domain.usecase.GetBalanceByDateAndTypeUseCase
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
    private val changeTransactionFilterUseCase: ChangeTransactionFilterUseCase,
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
            SuspendableResult.of<Unit, Exception> {
                goToPreviousDateUseCase(currentDate)
            }.fold(
                success = {
                    getBalance()
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

    fun goToNextMonth() {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> {
                goToNextDateUseCase(currentDate)
            }.fold(
                success = {
                    getBalance()
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

    fun addTransaction() {
        viewModelScope.launch {
            balance.id?.also { idBalance ->
                createTransaction(idBalance)
            } ?: run {
                SuspendableResult.of<Long, Exception> {
                    createBalanceUseCase(balance)
                }.fold(
                    success = {
                        createTransaction(it)
                    },
                    failure = {

                    }
                )
            }
        }
    }

    fun changeHideOption() {
        viewModelScope.launch {
            setViewState {
                it.copy(isHideLoading = true)
            }
            balance.id?.also {
                SuspendableResult.of<Unit, Exception> {
                    changeHideOptionUseCase(it, balance.isHide.not())
                }.fold(
                    success = {
                        balance.isHide = balance.isHide.not()
                        setViewState {
                            it.copy(
                                isHide = balance.isHide,
                                isHideLoading = false
                            )
                        }
                    },
                    failure = {
                        setViewState {
                            it.copy(isHideLoading = false)
                        }
                    }
                )
            } ?: run {
                setViewState {
                    it.copy(isHideLoading = false)
                }
            }
        }
    }

    fun changeTransactionFilter(id: Int) {
        viewModelScope.launch {
            setViewState {
                it.copy(
                    isLoading = true,
                    isTransactionsVisible = false,
                    isEmptyStateVisible = false,
                    isEmptyStateFilterTransactionVisible = false,
                    isChipsEnabled = false
                )
            }
            balance.transactions?.also {
                SuspendableResult.of<List<Transaction>, Exception> {
                    changeTransactionFilterUseCase(it, id)
                }.fold(
                    success = {
                        OrganizerActions.UpdateTransactions(it).sendAction()
                        setViewState { viewState ->
                            viewState.copy(
                                isLoading = false,
                                isTransactionsVisible = it.isNotEmpty(),
                                isEmptyStateFilterTransactionVisible = it.isEmpty(),
                                isChipsEnabled = true
                            )
                        }
                    },
                    failure = {
                        setViewState { viewState ->
                            viewState.copy(
                                isLoading = false,
                                isTransactionsVisible = false,
                                isEmptyStateFilterTransactionVisible = true,
                                isChipsEnabled = true
                            )
                        }
                    }
                )
            }
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
            SuspendableResult.of<Unit, Exception> {
                removeTransactionUseCase(transaction)
            }.fold(
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
                    isLoading = true,
                    isTransactionsVisible = false,
                    isChipsEnabled = false,
                    isEmptyStateFilterTransactionVisible = false,
                    isEmptyStateVisible = false
                )
            }
            SuspendableResult.of<Balance, Exception> {
                getBalanceByDateAndTypeUseCase(currentDate)
            }.fold(
                success = {
                    OrganizerActions.ShowBalance(it).sendAction()
                    setViewState { viewState ->
                        viewState.copy(
                            isLoading = false,
                            isHide = it.isHide,
                            isTransactionsVisible = it.transactions?.isNotEmpty() ?: false,
                            isEmptyStateVisible = it.transactions?.isEmpty() ?: true
                        )
                    }
                    setBalance(it)
                },
                failure = {
                    resetDate()
                    Timber.e(it)
                }
            )
        }
    }

    private suspend fun createTransaction(idBalance: Long) {
        SuspendableResult.of<Long, Exception> {
            val number = Random.nextInt(0, 10)
            val transaction = Transaction(
                idBalance = idBalance,
                icoResource = if (number > 5) "ic_salary" else "ic_regular",
                description = "Transacao Exemplo $number",
                date = currentDate.time,
                money = 200.0 * number.toDouble(),
                typeTransaction = if (number > 5) TypeTransactionEnum.INCOME else TypeTransactionEnum.SPENT
            )
            createTransactionUseCase(
                transaction
            )
        }.fold(
            success = {
                getBalance()
            },
            failure = {

            }
        )
    }

    private fun resetTransactionFilter() {
        OrganizerActions.ResetTransactionsFilter.sendAction()
    }

    private fun resetDate() {
        viewModelScope.launch {
            SuspendableResult.of<Unit, Exception> {
                resetDateUseCase(currentDate)
            }.fold(
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

    private fun setBalance(balance: Balance) {
        this.balance = balance
        this.balance.date = currentDate.time
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