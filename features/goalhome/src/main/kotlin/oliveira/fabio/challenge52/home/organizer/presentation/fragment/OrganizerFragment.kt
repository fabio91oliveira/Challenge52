package oliveira.fabio.challenge52.home.organizer.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import features.goalhome.R
import kotlinx.android.synthetic.main.fragment_organizer.*
import oliveira.fabio.challenge52.extensions.stylizeTextCurrency
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.home.organizer.presentation.adapter.TransactionAdapter
import oliveira.fabio.challenge52.presentation.vo.Balance
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum
import java.util.*

internal class OrganizerFragment : Fragment(R.layout.fragment_organizer) {

    private val transactionAdapter by lazy { TransactionAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setupRecyclerview()
        setupTextsStyle()
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
        txtBalance.text = 3000.0.toStringMoney(currentLocale = balance.currentLocale)
        txtIncome.text = 4000.0.toStringMoney(currentLocale = balance.currentLocale)
        txtSpent.text = 1000.0.toStringMoney(currentLocale = balance.currentLocale)
    }

    private fun setupObservables() {

    }

    private fun setupTextsStyle() {
        txtBalance.stylizeTextCurrency()
        txtIncome.stylizeTextCurrency()
        txtSpent.stylizeTextCurrency()
    }

    companion object {
        fun newInstance() =
            OrganizerFragment()
    }
}