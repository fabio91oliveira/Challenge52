package oliveira.fabio.challenge52.organizer.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.home.organizer.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_transaction.*
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum
import java.util.*

internal class TransactionAdapter :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var list: MutableList<Transaction> = mutableListOf()
    private var currentLocale: Locale = Locale.getDefault()

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        )

    fun addList(transactions: List<Transaction>) {
        list.addAll(transactions)
        notifyDataSetChanged()
    }

    fun setLocale(locale: Locale) {
        currentLocale = locale
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(transaction: Transaction) {
            txtDescription.text = transaction.description
            txtDate.text = transaction.description
            when (transaction.typeTransaction) {
                TypeTransactionEnum.INCOME -> {
                    txtValue.setTextColor(
                        ContextCompat.getColor(
                            containerView.context,
                            R.color.color_green
                        )
                    )
                    txtValue.text =
                        "+ ${transaction.money.toStringMoney(currentLocale = currentLocale)}"
                }
                TypeTransactionEnum.SPENT -> {
                    txtValue.setTextColor(
                        ContextCompat.getColor(
                            containerView.context,
                            R.color.color_red
                        )
                    )
                    txtValue.text =
                        "- ${transaction.money.toStringMoney(currentLocale = currentLocale)}"
                }
            }
        }
    }
}