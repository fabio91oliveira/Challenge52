package oliveira.fabio.challenge52.presentation.ui.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import features.goaldetails.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_week_header.*
import kotlinx.android.synthetic.main.item_week_subitem_details.*
import kotlinx.android.synthetic.main.item_week_subitem_week.*
import oliveira.fabio.challenge52.domain.model.vo.Item
import oliveira.fabio.challenge52.domain.model.vo.SubItemDetails
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.extensions.toCurrency
import oliveira.fabio.challenge52.extensions.toCurrentDateSystemString
import oliveira.fabio.challenge52.persistence.model.entity.Week
import java.text.DateFormat


class WeeksAdapter(private val onClickWeekListener: OnClickWeekListener, private val isDoneGoal: Boolean) :
    RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    private var list: MutableList<Item> = mutableListOf()

    override fun getItemViewType(position: Int) = list[position].viewType
    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) = holder.bind(list[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder = when (viewType) {
        Item.HEADER_ITEM -> HeaderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_week_header,
                parent,
                false
            )
        )
        Item.SUB_ITEM_DETAILS -> SubItemDetailsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_week_subitem_details, parent, false)
        )
        else -> SubItemWeekViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_week_subitem_week, parent, false)
        )
    }

    fun addSingleItem(item: Item, position: Int) {
        list.removeAt(position)
        list.add(position, item)
        notifyItemChanged(position)
    }

    fun addList(weeksList: List<Item>) {
        this.list.addAll(weeksList)
        notifyDataSetChanged()
    }

    fun clearList() {
        list.clear()
    }

    inner class HeaderViewHolder(override val containerView: View) :
        WeekViewHolder(containerView) {
        override fun bind(item: Item) {
            txtMonth.text = item.getHeader().title
        }

    }

    inner class SubItemDetailsViewHolder(override val containerView: View) : WeekViewHolder(containerView) {
        override fun bind(item: Item) {
            animatePercents(item.getDetails())
            txtWeeksCompleted.text = item.getDetails().remainingWeeks.toString()
            txtWeeksCompletedContinue.text =
                containerView.context.getString(R.string.goal_details_of_weeks, item.getDetails().totalWeeks.toString())
            txtGoalCompleted.text = item.getDetails().totalAccumulated.toCurrency()
            txtGoalCompletedContinue.text = containerView.context.getString(
                R.string.goal_details_of_money,
                item.getDetails().totalMoney.toCurrency()
            )

        }

        private fun animatePercents(subItemDetails: SubItemDetails) {
            val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, subItemDetails.totalPercent)
            animation.duration = 1000
            animation.interpolator = DecelerateInterpolator()
            animation.addUpdateListener {
                val progress = it.animatedValue as Int
                txtPercent.text =
                    progress.toString() + containerView.context.getString(R.string.progress_value_percent)
            }
            animation.start()
        }
    }

    inner class SubItemWeekViewHolder(override val containerView: View) : WeekViewHolder(containerView) {
        override fun bind(item: Item) {
            val animChecked = AnimationUtils.loadAnimation(containerView.context, R.anim.scale_fab_in)
            val animNotChecked = AnimationUtils.loadAnimation(containerView.context, R.anim.scale_fab_out)

            if (item.getWeek().week.isDeposited) {
                if (imgNotChecked.visibility != View.INVISIBLE) {
                    imgNotChecked.visibility = View.INVISIBLE
                }
                if (imgChecked.visibility != View.VISIBLE) {
                    imgChecked.visibility = View.VISIBLE
                }
            } else {
                if (imgChecked.visibility != View.INVISIBLE) {
                    imgChecked.visibility = View.INVISIBLE
                }
                if (imgNotChecked.visibility != View.VISIBLE) {
                    imgNotChecked.visibility = View.VISIBLE
                }
            }

            txtWeek.text =
                containerView.context.getString(R.string.goal_details_week, item.getWeek().week.position.toString())
            txtMoney.text = item.getWeek().week.spittedValue.toCurrency()
            txtDate.text =
                item.getWeek().week.date.toCurrentDateSystemString(DateFormat.SHORT)

            when (isDoneGoal) {
                false -> {
                    containerView.setOnClickListener {
                        it.doPopAnimation(100) {
                            onClickWeekListener.onClickWeek(item.getWeek().week, adapterPosition) {
                                when (item.getWeek().week.isDeposited) {
                                    true -> {
                                        if (imgChecked.visibility != View.INVISIBLE) {
                                            imgChecked.startAnimation(animNotChecked)
                                            imgChecked.visibility = View.INVISIBLE
                                        }
                                        if (imgNotChecked.visibility != View.VISIBLE) {
                                            imgNotChecked.startAnimation(animChecked)
                                            imgNotChecked.visibility = View.VISIBLE
                                        }
                                    }
                                    false -> {
                                        if (imgNotChecked.visibility != View.INVISIBLE) {
                                            imgNotChecked.startAnimation(animNotChecked)
                                            imgNotChecked.visibility = View.INVISIBLE
                                        }
                                        if (imgChecked.visibility != View.VISIBLE) {
                                            imgChecked.startAnimation(animChecked)
                                            imgChecked.visibility = View.VISIBLE
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    abstract class WeekViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        abstract fun bind(item: Item)
    }

    interface OnClickWeekListener {
        fun onClickWeek(week: Week, position: Int, func: () -> Unit)
    }
}