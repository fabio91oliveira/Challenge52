package oliveira.fabio.challenge52.presentation.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.goaldetails.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_header.*
import kotlinx.android.synthetic.main.item_to_save.*
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.extensions.toStringCurrentDateWithFormat
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import java.text.DateFormat


internal class ItemsAdapter(
    private val onClickItemListener: OnClickItemListener,
    private val isFromDoneGoal: Boolean
) :
    RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    private var list: MutableList<AdapterItem<String, ItemDetail>> = mutableListOf()
    private var lastClickTime = System.currentTimeMillis()

    override fun getItemViewType(position: Int) = list[position].viewType.type
    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        when (viewType) {
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

    fun addList(adapterItemDetailList: List<AdapterItem<String, ItemDetail>>) {
        list.addAll(adapterItemDetailList)
    }

    fun clearList() {
        list.clear()
    }

    inner class HeaderViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(itemDetail: AdapterItem<String, ItemDetail>) {
            txtPeriod.text = itemDetail.first
        }
    }

    inner class SubItemViewHolder(override val containerView: View) :
        ItemViewHolder(containerView) {
        override fun bind(itemDetail: AdapterItem<String, ItemDetail>) {
            itemDetail.second?.also { item ->
                if (item.isChecked)
                    bindCheck()
                else
                    bindUncheck()

                txtItem.text =
                    "${containerView.resources.getString(item.periodRes)} ${item.position}"
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
                            onClickItemListener.onClickItem(item, adapterPosition)
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
            line.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
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
            line.background.setColorFilter(color, PorterDuff.Mode.SRC)
        }
    }

    abstract class ItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        abstract fun bind(itemDetail: AdapterItem<String, ItemDetail>)
    }

    interface OnClickItemListener {
        fun onClickItem(itemDetail: ItemDetail, position: Int)
    }

    companion object {
        private const val PROGRESS_TAG = "progress"
        private const val INITIAL_VALUE = 0
        private const val PROGRESS_ANIMATION_DURATION = 1000L
        private const val POP_ANIMATION_DURATION = 100L
        private const val CLICK_TIME_INTERVAL = 1000L
    }
}