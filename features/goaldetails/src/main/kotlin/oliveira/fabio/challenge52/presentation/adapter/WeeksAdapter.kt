package oliveira.fabio.challenge52.presentation.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.goaldetails.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_details.*
import kotlinx.android.synthetic.main.item_header.*
import kotlinx.android.synthetic.main.item_week.*
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.extensions.toCurrency
import oliveira.fabio.challenge52.extensions.toCurrentDateSystemString
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import java.text.DateFormat

internal class WeeksAdapter(
    private val onClickWeekListener: OnClickWeekListener,
    private val isFromDoneGoal: Boolean
) :
    RecyclerView.Adapter<WeeksAdapter.ItemViewHolder>() {

    private var list: MutableList<AdapterItem<TopDetails, String, Week>> = mutableListOf()
    private var lastPositionClicked = 0

    override fun getItemViewType(position: Int) = list[position].viewType.type
    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        when (viewType) {
            AdapterItem.ViewType.DETAILS.type -> DetailsViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_details,
                    parent,
                    false
                )
            )
            AdapterItem.ViewType.HEADER.type -> HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_header,
                    parent,
                    false
                )
            )
            else -> WeekViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_week,
                    parent,
                    false
                )
            )
        }

    fun addList(adapterItemList: List<AdapterItem<TopDetails, String, Week>>) {
        list.addAll(adapterItemList)
        notifyDataSetChanged()
    }

    fun addItem(week: Week) {
        list[lastPositionClicked] = list[lastPositionClicked].copy(
            third = week
        )
        notifyItemChanged(lastPositionClicked)
    }

    inner class DetailsViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(item: AdapterItem<TopDetails, String, Week>) {
            item.first?.also {
                txtWeeksCompleted.text = it.totalCompletedWeeks.toString()
                txtGoalCompleted.text = it.totalMoneySaved

                ObjectAnimator.ofInt(
                    progressBar,
                    "progress",
                    0,
                    it.totalPercentsCompleted
                ).apply {
                    duration = 1000L
                    interpolator = DecelerateInterpolator()
                    addUpdateListener { valueAnimator ->
                        val progress = valueAnimator.animatedValue as Int
                        txtPercent.text = progress.toString()
                    }
                    start()
                }
            }
        }
    }

    inner class HeaderViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(item: AdapterItem<TopDetails, String, Week>) {
            chHeader.text = item.second
        }
    }

    inner class WeekViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(item: AdapterItem<TopDetails, String, Week>) {
            item.third?.also { week ->
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
        abstract fun bind(item: AdapterItem<TopDetails, String, Week>)
    }

    interface OnClickWeekListener {
        fun onClickWeek(week: Week)
    }
}