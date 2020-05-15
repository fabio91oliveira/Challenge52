package oliveira.fabio.challenge52.organizer.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.home.organizer.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_transaction.*
import oliveira.fabio.challenge52.extensions.getDateStringByFormat
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.presentation.vo.Transaction
import oliveira.fabio.challenge52.presentation.vo.enums.TypeTransactionEnum
import java.util.*

internal class TransactionAdapter(private val onSwipeLeftTransactionListener: OnSwipeLeftTransactionListener) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    private var list: LinkedList<Transaction> = LinkedList()
    private var currentLocale: Locale = Locale.getDefault()

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        )

    fun setLocale(locale: Locale) {
        currentLocale = locale
    }

    fun addList(transactions: List<Transaction>) {
        list.addAll(transactions)
    }

    fun clearList() {
        list.clear()
    }

    fun getTransactionByPosition(position: Int) = list[position]

    fun removeTransaction(position: Int) {
        onSwipeLeftTransactionListener.onDeleteTransaction(list[position], position)
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(transaction: Transaction) {
            txtDescription.text = transaction.description
            txtDate.text = transaction.date.getDateStringByFormat(DATE_PATTERN)

            val image = try {
                containerView.context.resources.getIdentifier(
                    transaction.icoResource, RESOURCE_TYPE,
                    containerView.context.packageName
                ).let {
                    if (it == 0) R.drawable.ic_no_avatar else it
                }
            } catch (ex: Exception) {
                R.drawable.ic_no_avatar
            }

            imgTransaction.setImageResource(image)

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

    interface OnSwipeLeftTransactionListener {
        fun onDeleteTransaction(transaction: Transaction, position: Int)
    }

    companion object {
        private const val DATE_PATTERN = "dd MMMM yyyy"
        private const val RESOURCE_TYPE = "drawable"
    }
}