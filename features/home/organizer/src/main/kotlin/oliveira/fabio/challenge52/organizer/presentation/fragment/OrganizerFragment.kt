package oliveira.fabio.challenge52.organizer.presentation.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import features.home.organizer.R
import kotlinx.android.synthetic.main.fragment_organizer.*
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.organizer.presentation.action.OrganizerActions
import oliveira.fabio.challenge52.organizer.presentation.adapter.SwipeToDeleteCallback
import oliveira.fabio.challenge52.organizer.presentation.adapter.TransactionAdapter
import oliveira.fabio.challenge52.organizer.presentation.viewmodel.OrganizerViewModel
import oliveira.fabio.challenge52.organizer.presentation.viewstate.Dialog
import oliveira.fabio.challenge52.organizer.presentation.vo.TypeOfTransactionEnum
import oliveira.fabio.challenge52.presentation.dialogfragment.PopupDialog
import oliveira.fabio.challenge52.presentation.view.SelectHeaderView
import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class OrganizerFragment : Fragment(R.layout.fragment_organizer),
    SelectHeaderView.ClickButtonsListener, TransactionAdapter.OnSwipeLeftTransactionListener {

    private val transactionAdapter by lazy { TransactionAdapter(this) }

    private val organizerViewModel: OrganizerViewModel by viewModel {
        parametersOf(
            Calendar.getInstance(),
            Balance()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onClickLeftListener() {
        organizerViewModel.goToPreviousMonth()
    }

    override fun onClickRightListener() {
        organizerViewModel.goToNextMonth()
    }

    override fun onDeleteTransaction(transaction: Transaction) {
        organizerViewModel.removeTransaction(transaction)
    }

    private fun init() {
        setupRecyclerview()
        setupScrollView()
        setupSelectHeaderView()
        setupChip()
        setupClickListener()
        setupChipClickListener()
        setupObservables()
    }

    private fun setupRecyclerview() {
        with(rvTransactions) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            adapter = transactionAdapter
            val swipeHandler = object : SwipeToDeleteCallback(context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    organizerViewModel.showConfirmationDialogRemoveTransaction(viewHolder.adapterPosition)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    private fun setupScrollView() {
        content.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                organizerViewModel.hideAddButton()
            } else {
                organizerViewModel.showAddButton()
            }
        })
    }

    private fun setupSelectHeaderView() {
        selectHeaderView.setClickButtonsListener(this)
    }

    private fun setupChip() {
        // TODO IMPROVE IT
        chipGroupTransactions.isSingleSelection = true
        TypeOfTransactionEnum.values().forEach {
            (layoutInflater.inflate(R.layout.item_chip, null) as Chip).apply {
                text = resources.getString(it.resStringDefault)
                id = it.resId
                tag = it.value
                chipGroupTransactions.addView(this)
                if (it == TypeOfTransactionEnum.ALL) chipGroupTransactions.check(id)
            }
        }
    }

    private fun setupClickListener() {
        viewClickHide.setOnClickListener {
            organizerViewModel.changeHideOption()
        }
        fabAdd.setOnClickListener {
            organizerViewModel.addTransaction()
        }
    }

    private fun setupChipClickListener() {
        chipGroupTransactions.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId).tag as Int
            organizerViewModel.changeTransactionFilter(chip)
        }
    }

    private fun setupObservables() {
        with(organizerViewModel) {
            organizerActions.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it) {
                    is OrganizerActions.ShowBalance -> {
                        setBalance(it.balance)
                        setTransactions(it.balance)
                        setFiltersCount(it.balance)

                    }
                    is OrganizerActions.UpdateTransactions -> {
                        updateTransactions(it.transactions)
                    }
                    is OrganizerActions.ResetTransactionsFilter -> {
                        resetTransactionFilter()
                    }
                }
            })
            organizerViewState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                showTransactions(it.isTransactionsVisible)
                showEmptyState(it.isEmptyStateVisible)
                showLoading(it.isLoading)
                setupHide(it.isHide)
                showLoadingHide(it.isHideLoading)
                setTextInSelectHeaderView(it.currentMonthYear)
                showAddButton(it.isAddButtonVisible)
                showTransactionFilterEmptyState(it.isEmptyStateFilterTransactionVisible)
                enableChips(it.isChipsEnabled)
                handleDialog(it.dialog)
            })
        }
    }

    private fun setBalance(balance: Balance) {
        with(balance) {
            txtIncome.doIncreaseMoneyAnimation(
                totalIncomes,
                currentLocale
            )
            txtSpent.doIncreaseMoneyAnimation(
                totalSpent,
                currentLocale
            )
            txtBalance.doIncreaseMoneyAnimation(
                total,
                currentLocale
            )
            setBalanceTextColor(total)
        }
    }

    private fun setTransactions(balance: Balance) {
        balance.transactions?.also {
            transactionAdapter.setLocale(balance.currentLocale)
            updateTransactions(it)
        } ?: run {
            transactionAdapter.clearList()
        }
    }

    private fun setFiltersCount(balance: Balance) {
        chipGroupTransactions.findViewById<Chip>(TypeOfTransactionEnum.ALL.resId).text =
            getString(TypeOfTransactionEnum.ALL.resStringParams, balance.totalAllFilter)

        chipGroupTransactions.findViewById<Chip>(TypeOfTransactionEnum.INCOME.resId).text =
            getString(TypeOfTransactionEnum.INCOME.resStringParams, balance.totalIncomeFilter)

        chipGroupTransactions.findViewById<Chip>(TypeOfTransactionEnum.SPENT.resId).text =
            getString(TypeOfTransactionEnum.SPENT.resStringParams, balance.totalSpentFilter)
    }


    private fun updateTransactions(transactions: List<Transaction>) {
        transactionAdapter.clearList()
        transactionAdapter.addList(transactions)
    }

    private fun resetTransactionFilter() {
        chipGroupTransactions.check(chipGroupTransactions.getChildAt(FIRST_ITEM).id)
    }

    private fun showTransactions(hasToShow: Boolean) {
        rvTransactions.isVisible = hasToShow
    }

    private fun showEmptyState(hasToShow: Boolean) {
        stateViewEmpty.isVisible = hasToShow
    }

    private fun showTransactionFilterEmptyState(hasToShow: Boolean) {
        stateViewTransactionsEmpty.isVisible = hasToShow
    }

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
    }

    private fun enableChips(hasToEnable: Boolean) {
        chipGroupTransactions.isEnabled = hasToEnable
    }

    private fun showAddButton(hasToShow: Boolean) =
        if (hasToShow) fabAdd.show() else fabAdd.hide()

    private fun showLoadingHide(hasToShow: Boolean) {
        progressHide.isVisible = hasToShow
        imgEyes.isVisible = hasToShow.not()
    }

    private fun setTextInSelectHeaderView(text: String) {
        selectHeaderView.setTitleText(text)
    }

    private fun setBalanceTextColor(value: Double) {
        context?.also {
            txtBalance.setTextColor(
                ContextCompat.getColor(
                    it,
                    if (value < 0) R.color.color_spent else android.R.color.white
                )
            )
        }
    }

    private fun handleDialog(dialogViewState: Dialog) {
        when (dialogViewState) {
            is Dialog.ConfirmationDialogRemoveTransaction -> {
                showDefaultDialog(
                    dialogViewState.imageRes,
                    dialogViewState.titleRes,
                    dialogViewState.descriptionRes,
                    dialogViewState.positionTransaction
                )
            }
        }
    }

    private fun setupHide(isHide: Boolean) {
        if (isHide) {
            groupMoney.visibility = View.INVISIBLE
            groupHide.visibility = View.VISIBLE
            imgEyes.setImageResource(R.drawable.ic_eye_off)
        } else {
            groupMoney.visibility = View.VISIBLE
            groupHide.visibility = View.GONE
            imgEyes.setImageResource(R.drawable.ic_eye_on)
        }
    }

    private fun TextView.doIncreaseMoneyAnimation(
        finalMoney: Double,
        currentLocale: Locale = Locale.getDefault()
    ) {
        ValueAnimator.ofFloat(0f, finalMoney.toFloat()).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 500
            addUpdateListener {
                val progress = it.animatedValue as Float
                text = if (progress == finalMoney.toFloat()) {
                    finalMoney.toStringMoney(currentLocale = currentLocale)
                } else {
                    progress.toStringMoney(currentLocale = currentLocale)
                }
            }
            start()
        }
    }

    private fun showDefaultDialog(
        @DrawableRes resImage: Int,
        @StringRes resTitle: Int,
        @StringRes resDescription: Int,
        position: Int
    ) =
        PopupDialog.Builder()
            .setTitle(resTitle)
            .setSubtitle(resDescription)
            .setupConfirmButtonColor(R.color.color_primary)
            .setImage(resImage)
            .setupConfirmButton(
                R.string.all_button_yes,
                object : PopupDialog.PopupDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        transactionAdapter.removeTransaction(position)
                    }
                }
            )
            .setupCancelButton(R.string.all_button_no,
                object : PopupDialog.PopupDialogCancelListener {
                    override fun onClickCancelButton() {
                        transactionAdapter.notifyDataSetChanged()
                        organizerViewModel.hideDialogs()
                    }
                })
            .build()
            .show(childFragmentManager, PopupDialog.TAG)

    companion object {
        fun newInstance() =
            OrganizerFragment()

        private const val FIRST_ITEM = 0
    }
}