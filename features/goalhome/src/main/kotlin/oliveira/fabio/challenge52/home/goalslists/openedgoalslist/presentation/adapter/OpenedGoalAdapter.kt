package oliveira.fabio.challenge52.home.goalslists.openedgoalslist.presentation.adapter

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.goalhome.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_goal.*
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.extensions.toCurrency
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks


class OpenedGoalAdapter(private val onClickGoalListener: OnClickGoalListener) :
    RecyclerView.Adapter<OpenedGoalAdapter.GoalViewHolder>() {

    private var lastPosition = 0
    private var goalsList: MutableList<GoalWithWeeks> = mutableListOf()

    override fun getItemCount() = goalsList.size
    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) = holder.bind(position)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GoalViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
    )

    fun addList(goalsList: List<GoalWithWeeks>) {
        this.goalsList.addAll(goalsList)
        notifyDataSetChanged()
    }

    fun clearList() {
        lastPosition = goalsList.size
        goalsList.clear()
        notifyDataSetChanged()
    }

    inner class GoalViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(position: Int) {
            txtName.text = goalsList[position].goal.name
            txtWeeksRemaining.text = containerView.resources.getString(
                R.string.goals_lists_weeks_remaining,
                goalsList[position].getWeeksDepositedCount().toString()
            )
            txtMoney.text = goalsList[position].getTotal().toCurrency()
            val progress = goalsList[position].getPercentOfConclusion()

            if (progress > 0) {
                txtPercent.setTextColor(
                    ContextCompat.getColor(
                        containerView.context,
                        R.color.color_accent
                    )
                )
            } else {
                txtPercent.setTextColor(
                    ContextCompat.getColor(
                        containerView.context,
                        android.R.color.black
                    )
                )
            }

            val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, progress)
            animation.duration = 1000
            animation.interpolator = DecelerateInterpolator()
            animation.addUpdateListener {
                val prog = it.animatedValue as Int
                txtPercent.text =
                    prog.toString() + containerView.context.getString(R.string.progress_value_percent)
            }
            animation.start()

//            if (position >= lastPosition) animate()

            containerView.setOnClickListener {
                it.doPopAnimation(100) {
                    onClickGoalListener.onClickGoal(goalsList[position])
                }
            }
        }

        private fun animate() {
            val valueAnimator = ValueAnimator.ofFloat(-500f, 0f)
            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
            valueAnimator.duration = 500
            valueAnimator.addUpdateListener {
                val progress = it.animatedValue as Float
                containerView.translationX = progress
            }
            valueAnimator.start()
            lastPosition++
        }
    }

    interface OnClickGoalListener {
        fun onClickGoal(goal: GoalWithWeeks)
    }
}