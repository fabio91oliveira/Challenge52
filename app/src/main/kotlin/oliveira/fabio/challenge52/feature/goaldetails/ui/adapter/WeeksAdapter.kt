package oliveira.fabio.challenge52.feature.goaldetails.ui.adapter

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_week.*
import kotlinx.android.synthetic.main.item_week.view.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.model.entity.Week
import oliveira.fabio.challenge52.util.extension.getDayNumber
import oliveira.fabio.challenge52.util.extension.getMonthNumber
import oliveira.fabio.challenge52.util.extension.getYearNumber
import oliveira.fabio.challenge52.util.extension.toCurrency


class WeeksAdapter(private val onClickWeekListener: OnClickWeekListener) :
    RecyclerView.Adapter<WeeksAdapter.WeekViewHolder>() {

    private var weeksList: MutableList<Week> = mutableListOf()
    private var lastPosition = 0

    override fun getItemCount() = weeksList.size
    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) = holder.bind(position)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WeekViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_week, parent, false)
    )

    fun addList(weeksList: List<Week>) {
        this.weeksList.addAll(weeksList)
        notifyDataSetChanged()
    }

    inner class WeekViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(position: Int) {
            chkWeek.isChecked = weeksList[position].isDeposited
            txtWeek.text = weeksList[position].position.toString()
            txtValue.text = weeksList[position].spittedValue.toCurrency()
            txtYear.text = weeksList[position].date.getYearNumber().toString()
            txtMonth.text = weeksList[position].date.getMonthNumber().toString()
            txtDay.text = weeksList[position].date.getDayNumber().toString()

            containerView.setOnClickListener {
                weeksList[position].isDeposited = !weeksList[position].isDeposited
                onClickWeekListener.onClickWeek(weeksList[position])
            }

            if (position >= lastPosition) animate()
        }

        private fun animate() {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator()
            fadeIn.duration = 900

            val animation = AnimationSet(false)
            animation.addAnimation(fadeIn)
            cardWeek.animation = animation

            val valueAnimator = ValueAnimator.ofFloat(-300f, 0f)
            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
            valueAnimator.duration = 500
            valueAnimator.addUpdateListener {
                val progress = it.animatedValue as Float
                cardWeek.translationX = progress
            }
            valueAnimator.start()
            lastPosition++
        }

        private fun changeBackground(week: Week) {
            var firstColor = 0
            var secondColor = 0

            when (week.isDeposited) {
                true -> {
                    week.isDeposited = false
                    firstColor = ContextCompat.getColor(containerView.context, R.color.colorGreen)
                    secondColor = ContextCompat.getColor(containerView.context, R.color.colorWhite)
                }
                false -> {
                    week.isDeposited = true
                    firstColor = ContextCompat.getColor(containerView.context, R.color.colorWhite)
                    secondColor = ContextCompat.getColor(containerView.context, R.color.colorGreen)
                }
            }

            val duration = 500L
            ObjectAnimator.ofObject(
                containerView.cardWeek,
                "cardBackgroundColor",
                ArgbEvaluator(),
                firstColor,
                secondColor
            )
                .setDuration(duration)
                .start()
        }
    }

    interface OnClickWeekListener {
        fun onClickWeek(week: Week)
    }
}