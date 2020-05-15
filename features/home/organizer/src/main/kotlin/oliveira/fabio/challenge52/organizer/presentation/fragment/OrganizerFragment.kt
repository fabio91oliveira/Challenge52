package oliveira.fabio.challenge52.organizer.presentation.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import features.home.organizer.R
import kotlinx.android.synthetic.main.fragment_organizer.*
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.organizer.presentation.action.OrganizerActions
import oliveira.fabio.challenge52.organizer.presentation.adapter.SwipeToDeleteCallback
import oliveira.fabio.challenge52.organizer.presentation.adapter.TransactionAdapter
import oliveira.fabio.challenge52.organizer.presentation.enums.FilterEnum
import oliveira.fabio.challenge52.organizer.presentation.viewmodel.OrganizerViewModel
import oliveira.fabio.challenge52.organizer.presentation.vo.BalanceBottom
import oliveira.fabio.challenge52.organizer.presentation.vo.BalanceTop
import oliveira.fabio.challenge52.presentation.view.SelectHeaderView
import oliveira.fabio.challenge52.presentation.vo.Transaction
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class OrganizerFragment : Fragment(R.layout.fragment_organizer),
    SelectHeaderView.ClickButtonsListener, TransactionAdapter.OnSwipeLeftTransactionListener {

    private val transactionAdapter by lazy { TransactionAdapter(this) }

    private val organizerViewModel: OrganizerViewModel by viewModel()

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

    override fun onDeleteTransaction(transaction: Transaction, position: Int) {
        organizerViewModel.removeTransaction(transaction, position)
    }

    private fun init() {
        setupRecyclerview()
        setupScrollView()
        setupSwipeRefreshLayout()
        setupSelectHeaderView()
        setupChip()
        setupClickListener()
        setupChipClickListener()
        setupObservables()
    }

    private fun setupRecyclerview() {
        with(rvTransactions) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = transactionAdapter
            val swipeHandler = object : SwipeToDeleteCallback(context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val transaction =
                        transactionAdapter.getTransactionByPosition(viewHolder.adapterPosition)

                    organizerViewModel.removeTransaction(
                        transaction,
                        viewHolder.adapterPosition
                    )
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

    private fun setupSwipeRefreshLayout() {
        with(srlBalance) {
            setColorSchemeResources(
                android.R.color.white
            )
            setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_primary
                )
            )
            setOnRefreshListener {
                organizerViewModel.getBalance()
            }
        }
    }

    private fun setupSelectHeaderView() {
        selectHeaderView.setClickButtonsListener(this)
    }

    private fun setupChip() {
        FilterEnum.values().forEach {
            val chip = chipGroupTransactions.findViewById<Chip>(it.resId)
            chip.tag = it.tag
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
            val chip = group.findViewById<Chip>(checkedId).tag as String
            organizerViewModel.changeTransactionFilter(chip)
        }
    }

    private fun setupObservables() {
        with(organizerViewModel) {
            organizerActions.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it) {
                    is OrganizerActions.UpdateTransactions -> {
                        refreshTransaction()
                    }
                    is OrganizerActions.UpdateTransactionsAfterRemove -> {
                        refreshTransactionAfterRemove(it.position)
                    }
                    OrganizerActions.ResetFilters -> {
                        resetSelectionFilters()
                    }
                    is OrganizerActions.ShowConfirmationMessage -> {
                        showSnackBar(it.strRes)
                    }
                }
            })
            organizerViewState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                showTransactions(it.isTransactionsVisible)
                showLoadingFilters(it.isLoadingFilters)
                showFilters(it.isFiltersVisible)
                showEmptyState(it.isEmptyStateVisible)
                showLoadingBalance(it.isLoadingBalance)
                showLoadingTransactions(it.isLoadingTransactions)
                setupHide(it.isHide)
                showLoadingHide(it.isHideLoading)
                setTextInSelectHeaderView(it.currentMonthYear)
                showAddButton(it.isAddButtonVisible)
                showTransactionFilterEmptyState(it.isEmptyStateFilterTransactionVisible)
            })
            organizerBalanceViewState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                setBalance(it.balanceTop)
            })
            organizerTransactionsViewState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                setFiltersCounters(it.balanceBottom)
                setTransactions(it.balanceBottom)
            })
        }
    }

    private fun setBalance(balanceTop: BalanceTop) {
        with(balanceTop) {
            txtIncome.postOnAnimation {
                txtIncome.doIncreaseMoneyAnimation(
                    totalIncomes,
                    currentLocale
                )
            }
            txtSpent.postOnAnimation {
                txtSpent.doIncreaseMoneyAnimation(
                    totalSpent,
                    currentLocale
                )
            }
            txtBalance.postOnAnimation {
                setBalanceTextColor(total)
                txtBalance.doIncreaseMoneyAnimation(
                    total,
                    currentLocale
                )
            }
        }
    }

    private fun setTransactions(balanceBottom: BalanceBottom) {
        transactionAdapter.setLocale(balanceBottom.currentLocale)
        transactionAdapter.clearList()
        transactionAdapter.addList(balanceBottom.transactions)
    }

    private fun refreshTransaction() {
        transactionAdapter.notifyDataSetChanged()
    }

    private fun refreshTransactionAfterRemove(position: Int) {
        transactionAdapter.notifyItemRemoved(position)
    }

    private fun setFiltersCounters(balanceBottom: BalanceBottom) {
        chipGroupTransactions.findViewById<Chip>(FilterEnum.ALL.resId).text =
            getString(FilterEnum.ALL.resStringParams, balanceBottom.totalAllFilter)

        chipGroupTransactions.findViewById<Chip>(FilterEnum.INCOME.resId).text =
            getString(FilterEnum.INCOME.resStringParams, balanceBottom.totalIncomeFilter)

        chipGroupTransactions.findViewById<Chip>(FilterEnum.SPENT.resId).text =
            getString(FilterEnum.SPENT.resStringParams, balanceBottom.totalSpentFilter)
    }

    private fun resetSelectionFilters() {
        chipGroupTransactions.setOnCheckedChangeListener(null)
        chipGroupTransactions.check(chipGroupTransactions.getChildAt(FIRST_ITEM).id)
        setupChipClickListener()
    }

    private fun showTransactions(hasToShow: Boolean) {
        rvTransactions.isVisible = hasToShow
    }

    private fun showLoadingFilters(hasToShow: Boolean) {
        shimmerLayoutChip.isVisible = hasToShow
    }

    private fun showEmptyState(hasToShow: Boolean) {
        stateViewEmpty.isVisible = hasToShow
    }

    private fun showFilters(hasToShow: Boolean) {
        chipGroupTransactions.visibility = if (hasToShow) View.VISIBLE else View.INVISIBLE
    }

    private fun showTransactionFilterEmptyState(hasToShow: Boolean) {
        stateViewTransactionsEmpty.isVisible = hasToShow
    }

    private fun showLoadingBalance(hasToShow: Boolean) {
        shimmerLayoutTop.isVisible = hasToShow
        showRefreshing(hasToShow)
    }

    private fun showLoadingTransactions(hasToShow: Boolean) {
        shimmerLayoutTransactions.isVisible = hasToShow
        shimmerLayoutChip.isVisible = hasToShow
        showRefreshing(hasToShow)
    }

    private fun showRefreshing(hasToShow: Boolean) {
        srlBalance.isRefreshing = hasToShow
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

    private fun setSelectHeaverViewControlButtonsEnable(isEnabled: Boolean) {
        selectHeaderView.setControlButtonsEnabled(isEnabled)
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
            duration = 1000
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

    private fun showSnackBar(@StringRes strRes: Int) {
        context?.also {
            Snackbar.make(
                content,
                strRes,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun newInstance() =
            OrganizerFragment()

        private const val FIRST_ITEM = 0
    }
}