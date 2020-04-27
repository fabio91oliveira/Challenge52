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
import kotlinx.android.synthetic.main.item_to_save.*
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.extensions.stylizeTextCurrency
import oliveira.fabio.challenge52.extensions.toStringCurrentDateWithFormat
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.PeriodItemEnum
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import java.text.DateFormat


internal class ItemsAdapter(
    private val onClickItemListener: OnClickItemListener,
    private val isFromDoneGoal: Boolean
) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    private var list: MutableList<AdapterItem<TopDetails, String, ItemDetail>> = mutableListOf()
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
            else -> SubItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_to_save,
                    parent,
                    false
                )
            )
        }

    fun addList(adapterItemDetailList: List<AdapterItem<TopDetails, String, ItemDetail>>) {
        list.clear()
        list.addAll(adapterItemDetailList)
        notifyDataSetChanged()
    }

    inner class DetailsViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(itemDetail: AdapterItem<TopDetails, String, ItemDetail>) {
            itemDetail.first?.also {
                val period = when (it.periodItemEnum) {
                    PeriodItemEnum.DAY -> R.string.goal_details_days
                    PeriodItemEnum.WEEK -> R.string.goal_details_weeks
                    PeriodItemEnum.MONTH -> R.string.goal_details_months
                }

                txtItemsCompleted.text = containerView.resources.getString(
                    period,
                    it.totalCompletedItems.toString(), it.totalItems.toString()
                )
                txtMoneySaved.text = it.totalMoneySaved
                txtMoneySaved.stylizeTextCurrency()
                txtMoneyToSave.text = containerView.resources.getString(
                    R.string.goal_details_money,
                    it.totalMoneyToSave
                )
                txtMoneyToSave.stylizeTextCurrency()

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
        override fun bind(itemDetail: AdapterItem<TopDetails, String, ItemDetail>) {
            chHeader.text = itemDetail.second
        }
    }

    inner class SubItemViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(itemDetail: AdapterItem<TopDetails, String, ItemDetail>) {
            itemDetail.third?.also { item ->
                if (item.isChecked)
                    bindCheck()
                else
                    bindUncheck()

                val period = when (item.periodItemEnum) {
                    PeriodItemEnum.DAY -> R.string.goal_details_day
                    PeriodItemEnum.WEEK -> R.string.goal_details_week
                    PeriodItemEnum.MONTH -> R.string.goal_details_month
                }

                txtItem.text =
                    "${containerView.resources.getString(period)} ${item.position}"
                txtMoney.text = item.moneyToSave
                txtDate.text =
                    item.date.toStringCurrentDateWithFormat(DateFormat.SHORT)

                if (isFromDoneGoal.not())
                    containerView.setOnClickListener {
                        val now = System.currentTimeMillis()
                        if (now - lastClickTime < CLICK_TIME_INTERVAL) {
                            return@setOnClickListener
                        }
                        lastClickTime = now
                        it.doPopAnimation(POP_ANIMATION_DURATION) {
                            onClickItemListener.onClickItem(item)
                        }
                    }
            }
        }

        private fun bindCheck() {
            imgNotChecked.visibility =
                if (imgNotChecked.visibility != View.INVISIBLE) View.INVISIBLE else imgNotChecked.visibility
            imgChecked.visibility =
                if (imgChecked.visibility != View.VISIBLE) View.VISIBLE else imgChecked.visibility
            val color = ContextCompat.getColor(
                containerView.context,
                R.color.color_green
            )
            txtMoney.setTextColor(color)
            line.setBackgroundColor(color)
        }

        private fun bindUncheck() {
            imgChecked.visibility =
                if (imgChecked.visibility != View.INVISIBLE) View.INVISIBLE else imgChecked.visibility
            imgNotChecked.visibility =
                if (imgNotChecked.visibility != View.VISIBLE) View.VISIBLE else imgNotChecked.visibility
            val color = ContextCompat.getColor(
                containerView.context,
                R.color.color_transparent_grey
            )
            txtMoney.setTextColor(color)
            line.setBackgroundColor(color)
        }
    }

    abstract class ItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        abstract fun bind(itemDetail: AdapterItem<TopDetails, String, ItemDetail>)
    }

    interface OnClickItemListener {
        fun onClickItem(itemDetail: ItemDetail)
    }

    companion object {
        private const val PROGRESS_TAG = "progress"
        private const val INITIAL_VALUE = 0
        private const val PROGRESS_ANIMATION_DURATION = 1000L
        private const val POP_ANIMATION_DURATION = 100L
        private const val CLICK_TIME_INTERVAL = 1000L
    }
}