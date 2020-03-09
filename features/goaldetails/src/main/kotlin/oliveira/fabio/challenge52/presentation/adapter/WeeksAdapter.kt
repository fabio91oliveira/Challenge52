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
    private var lastClickTime = System.currentTimeMillis()

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

    fun updateWeek(week: Week) {
        list[lastPositionClicked] = list[lastPositionClicked].copy(
            third = week
        )
        notifyItemChanged(lastPositionClicked)
    }

    fun updateTopDetail(topDetails: TopDetails) {
        list[TOP_DETAILS_POSITION] = list[TOP_DETAILS_POSITION].copy(
            first = topDetails
        )
        notifyItemChanged(TOP_DETAILS_POSITION)
    }

    inner class DetailsViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(item: AdapterItem<TopDetails, String, Week>) {
            item.first?.also {
                txtWeeksCompleted.text = containerView.resources.getString(
                    R.string.goal_details_weeks,
                    it.totalCompletedWeeks.toString(), it.totalWeeks.toString()
                )
                txtMoneySaved.text = it.totalMoneySaved
                txtMoneyToSave.text = containerView.resources.getString(
                    R.string.goal_details_money,
                    it.totalMoneyToSave
                )

                ObjectAnimator.ofInt(
                    progressBar,
                    PROGRESS_TAG,
                    INITIAL_VALUE,
                    it.totalPercentsCompleted
                ).apply {
                    duration = PROGRESS_ANIMATION_DURATION
                    interpolator = DecelerateInterpolator()
                    addUpdateListener { valueAnimator ->
                        val progress = valueAnimator.animatedValue as Int
                        txtTotalPercent.text = progress.toString()
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
                        val now = System.currentTimeMillis()
                        if (now - lastClickTime < CLICK_TIME_INTERVAL) {
                            return@setOnClickListener
                        }
                        lastClickTime = now
                        lastPositionClicked = adapterPosition
                        it.doPopAnimation(POP_ANIMATION_DURATION) {
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

    companion object {
        private const val TOP_DETAILS_POSITION = 0
        private const val PROGRESS_TAG = "progress"
        private const val INITIAL_VALUE = 0
        private const val PROGRESS_ANIMATION_DURATION = 1000L
        private const val POP_ANIMATION_DURATION = 100L
        private const val CLICK_TIME_INTERVAL = 1000L
    }
}