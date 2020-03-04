package oliveira.fabio.challenge52.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.goaldetails.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_week_header.*
import kotlinx.android.synthetic.main.item_week_subitem_week.*
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.extensions.toCurrency
import oliveira.fabio.challenge52.extensions.toCurrentDateSystemString
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem
import java.text.DateFormat

internal class WeeksAdapter(
    private val onClickWeekListener: OnClickWeekListener,
    private val isFromDoneGoal: Boolean = false
) :
    RecyclerView.Adapter<WeeksAdapter.ItemViewHolder>() {

    private var list: MutableList<AdapterItem<String, Week>> = mutableListOf()
    private var lastPositionClicked = 0

    override fun getItemViewType(position: Int) = list[position].viewType.type
    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        when (viewType) {
            AdapterItem.ViewType.HEADER.type -> HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_week_header,
                    parent,
                    false
                )
            )
            else -> WeekViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_week_subitem_week,
                    parent,
                    false
                )
            )
        }

    fun addList(adapterItemList: List<AdapterItem<String, Week>>) {
        list.addAll(adapterItemList)
        notifyDataSetChanged()
    }

    fun addSingleItem(week: Week) {
        list[lastPositionClicked] = list[lastPositionClicked].copy(
            second = week
        )
        notifyItemChanged(lastPositionClicked)
    }

    inner class HeaderViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(item: AdapterItem<String, Week>) {
            chHeader.text = item.first
        }
    }

    inner class WeekViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(item: AdapterItem<String, Week>) {
            item.second?.also { week ->
                if (week.isChecked)
                    bindDepositedChecks()
                else
                    bindNotDepositedChecks()

                txtWeek.text =
                    containerView.context.getString(
                        R.string.goal_details_week,
                        week.weekNumber.toString()
                    )
                txtMoney.text = week.moneyToSave.toCurrency()
                txtDate.text =
                    week.date.toCurrentDateSystemString(DateFormat.SHORT)

                if (isFromDoneGoal.not())
                    containerView.setOnClickListener {
                        it.doPopAnimation(100) {
                            lastPositionClicked = adapterPosition
                            onClickWeekListener.onClickWeek(week)
                        }
                    }
            }
        }

        private fun bindDepositedChecks() {
            imgNotChecked.visibility =
                if (imgNotChecked.visibility != View.INVISIBLE) View.INVISIBLE else imgNotChecked.visibility
            imgChecked.visibility =
                if (imgChecked.visibility != View.VISIBLE) View.VISIBLE else imgChecked.visibility
            txtMoney.setTextColor(
                ContextCompat.getColor(
                    containerView.context,
                    R.color.color_green
                )
            )
        }

        private fun bindNotDepositedChecks() {
            imgChecked.visibility =
                if (imgChecked.visibility != View.INVISIBLE) View.INVISIBLE else imgChecked.visibility
            imgNotChecked.visibility =
                if (imgNotChecked.visibility != View.VISIBLE) View.VISIBLE else imgNotChecked.visibility
            txtMoney.setTextColor(
                ContextCompat.getColor(
                    containerView.context,
                    R.color.color_accent
                )
            )
        }
    }

    abstract class ItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        abstract fun bind(item: AdapterItem<String, Week>)
    }

    interface OnClickWeekListener {
        fun onClickWeek(week: Week)
    }
}