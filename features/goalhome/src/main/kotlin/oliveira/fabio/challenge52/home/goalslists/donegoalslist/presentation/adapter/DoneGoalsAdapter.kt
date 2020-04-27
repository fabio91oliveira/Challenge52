package oliveira.fabio.challenge52.home.goalslists.donegoalslist.presentation.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.goalhome.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_done_goal.*
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.extensions.stylizeTextCurrency
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.enums.PeriodEnum

internal class DoneGoalsAdapter(private val onClickGoalListener: OnClickGoalListener) :
    RecyclerView.Adapter<DoneGoalsAdapter.GoalViewHolder>() {

    private var goalsList: MutableList<Goal> = mutableListOf()

    override fun getItemCount() = goalsList.size
    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) = holder.bind(position)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GoalViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_done_goal, parent, false)
    )

    fun addList(goalsList: List<Goal>) {
        this.goalsList.addAll(goalsList)
        notifyDataSetChanged()
    }

    fun clearList() {
        goalsList.clear()
        notifyDataSetChanged()
    }

    inner class GoalViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(position: Int) {
            txtName.text = goalsList[position].name

            val period = when (goalsList[position].period) {
                PeriodEnum.DAILY -> R.string.goals_lists_days_remaining
                PeriodEnum.WEEKLY -> R.string.goals_lists_weeks_remaining
                PeriodEnum.MONTHLY -> R.string.goals_lists_months_remaining
            }

            txtCompletedItems.text = containerView.resources.getString(
                period,
                goalsList[position].getTotalItems().toString(), goalsList[position].items.size
            )

            txtMoney.text =
                goalsList[position].totalMoney.toStringMoney(currentLocale = goalsList[position].currentLocale)

            ObjectAnimator.ofInt(
                progressBar,
                PROGRESS_TAG,
                INITIAL_VALUE,
                goalsList[position].getTotalPercent()
            ).apply {
                duration = PROGRESS_ANIMATION_DURATION
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val progress = it.animatedValue as Int
                    when {
                        progress == FINAL_PERCENT -> {
                            val color = ContextCompat.getColor(
                                containerView.context,
                                R.color.color_green
                            )
                            txtMoney.setTextColor(color)
                            txtPercent.setTextColor(color)
                            progressBar.progressDrawable = ContextCompat.getDrawable(
                                containerView.context,
                                R.drawable.background_completed_progress_bar
                            )
                        }
                        progress > INITIAL_PERCENT -> {
                            val color = ContextCompat.getColor(
                                containerView.context,
                                R.color.color_red
                            )
                            txtMoney.setTextColor(color)
                            txtPercent.setTextColor(color)
                            progressBar.progressDrawable = ContextCompat.getDrawable(
                                containerView.context,
                                R.drawable.background_uncompleted_progress_bar
                            )
                        }
                        else -> {
                            val color = ContextCompat.getColor(
                                containerView.context,
                                android.R.color.black
                            )
                            txtMoney.setTextColor(color)
                            txtPercent.setTextColor(color)
                            progressBar.progressDrawable = ContextCompat.getDrawable(
                                containerView.context,
                                R.drawable.background_uncompleted_progress_bar
                            )
                        }
                    }
                    txtMoney.stylizeTextCurrency()
                    txtPercent.text =
                        progress.toString() + containerView.context.getString(R.string.progress_value_percent)
                }
                start()
            }
            containerView.setOnClickListener {
                it.doPopAnimation(POP_ANIMATION_DURATION) {
                    onClickGoalListener.onClickGoal(goalsList[position])
                }
            }
        }
    }

    interface OnClickGoalListener {
        fun onClickGoal(goal: Goal)
    }

    companion object {
        private const val PROGRESS_TAG = "progress"
        private const val INITIAL_VALUE = 0
        private const val INITIAL_PERCENT = 0
        private const val FINAL_PERCENT = 100
        private const val PROGRESS_ANIMATION_DURATION = 1000L
        private const val POP_ANIMATION_DURATION = 100L
    }
}