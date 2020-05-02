package oliveira.fabio.challenge52.organizer.presentation.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import features.home.organizer.R
import kotlinx.android.synthetic.main.fragment_organizer.*
import oliveira.fabio.challenge52.extensions.stylizeTextCurrency
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.organizer.presentation.adapter.TransactionAdapter
import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum
import java.util.*

class OrganizerFragment : Fragment(R.layout.fragment_organizer) {

    private val transactionAdapter by lazy { TransactionAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setupRecyclerview()
        setupClickListener()
        setupObservables()
    }

    private fun setupRecyclerview() {
        with(rvTransactions) {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = transactionAdapter
            itemAnimator = null
        }

        val a = Transaction(
            1,
            2,
            "Salary",
            Date(),
            30000.0,
            TypeTransactionEnum.INCOME
        )

        val b = Transaction(
            1,
            2,
            "Investiments",
            Date(),
            10.0,
            TypeTransactionEnum.SPENT
        )
        val c = Transaction(
            1,
            2,
            "Investiments",
            Date(),
            10.0,
            TypeTransactionEnum.SPENT
        )
        val d = Transaction(
            1,
            2,
            "Investiments",
            Date(),
            10.0,
            TypeTransactionEnum.SPENT
        )
        val e = Transaction(
            1,
            2,
            "Investiments",
            Date(),
            10.0,
            TypeTransactionEnum.SPENT
        )

        val balance = Balance(
            0,
            Date(),
            Locale.getDefault(),
            arrayListOf(a, b, c, d, e, b, c, d, e, b, c, d, e)
        )


        transactionAdapter.setLocale(balance.currentLocale)
        transactionAdapter.addList(balance.transactions)

        txtBalance.doIncreaseMoneyAnimation(3000.0, balance.currentLocale)
        txtIncome.doIncreaseMoneyAnimation(4000.0, balance.currentLocale)
        txtSpent.doIncreaseMoneyAnimation(1000.0, balance.currentLocale)
    }

    private fun setupClickListener() {
        imgEyes.setOnClickListener {

        }
    }

    private fun setupObservables() {

    }

    private fun TextView.doIncreaseMoneyAnimation(
        finalMoney: Double,
        currentLocale: Locale = Locale.getDefault()
    ) {
        ValueAnimator.ofFloat(0f, finalMoney.toFloat()).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 2300
            addUpdateListener {
                val progress = it.animatedValue as Float
                if (progress == finalMoney.toFloat()) {
                    text = finalMoney.toStringMoney(currentLocale = currentLocale)
                } else {
                    text = progress.toStringMoney(currentLocale = currentLocale)
                }
                stylizeTextCurrency()
            }
            start()
        }
    }

    companion object {
        fun newInstance() =
            OrganizerFragment()
    }
}