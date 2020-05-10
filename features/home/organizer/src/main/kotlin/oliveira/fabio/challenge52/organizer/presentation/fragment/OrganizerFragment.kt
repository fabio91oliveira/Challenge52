package oliveira.fabio.challenge52.organizer.presentation.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import features.home.organizer.R
import kotlinx.android.synthetic.main.fragment_organizer.*
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.organizer.presentation.action.OrganizerActions
import oliveira.fabio.challenge52.organizer.presentation.adapter.TransactionAdapter
import oliveira.fabio.challenge52.organizer.presentation.viewmodel.OrganizerViewModel
import oliveira.fabio.challenge52.presentation.view.SelectHeaderView
import oliveira.fabio.challenge52.presentation.vo.Balance
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class OrganizerFragment : Fragment(R.layout.fragment_organizer),
    SelectHeaderView.ClickButtonsListener {

    private val transactionAdapter by lazy { TransactionAdapter() }

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

    private fun init() {
        setupRecyclerview()
        setupScrollView()
        setupSelectHeaderView()
        setupClickListener()
        setupChipClickListener()
        setupObservables()
    }

    private fun setupRecyclerview() {
        with(rvTransactions) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = transactionAdapter
            itemAnimator = null
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

    private fun setupClickListener() {
        viewClickHide.setOnClickListener {
            organizerViewModel.changeHideOption()
        }
        fabAdd.setOnClickListener {
            organizerViewModel.addTransaction()
        }
    }

    private fun setupChipClickListener() {
        chipGroupTransactions.setOnCheckedChangeListener { _, checkedId ->

        }
    }

    private fun setupObservables() {
        with(organizerViewModel) {
            organizerActions.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it) {
                    is OrganizerActions.ShowBalance -> {
                        setBalance(it.balance)
                        setTransactions(it.balance)
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
            transactionAdapter.clearList()
            transactionAdapter.addList(it)
        } ?: run {
            transactionAdapter.clearList()
        }
    }

    private fun showTransactions(hasToShow: Boolean) {
        rvTransactions.isVisible = hasToShow
        chipGroupTransactions.isVisible = hasToShow
        viewLine.isVisible = hasToShow
        txtTransactions.isVisible = hasToShow
    }

    private fun showEmptyState(hasToShow: Boolean) {
        stateViewEmpty.isVisible = hasToShow
    }

    private fun showLoading(hasToShow: Boolean) {

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

    companion object {
        fun newInstance() =
            OrganizerFragment()
    }
}